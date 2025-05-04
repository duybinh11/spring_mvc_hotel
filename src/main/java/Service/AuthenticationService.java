package Service;


import Dto.Request.TokenRefreshRequest;
import Dto.Request.UserRequest;
import Dto.Response.TokenResponse;
import Entity.RevokedToken;
import Entity.UserEntity;
import Repository.UserRepository;
import Util.JwtUtil;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RevokedTokenService revokedTokenService;

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserRepository userRepository;


    public TokenResponse login(UserRequest userRequest) {
        try {
            List<SimpleGrantedAuthority> authoritiesUser =  userDetailService.permissions(userRequest.getEmail());
            System.out.println(authoritiesUser);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getEmail(),
                            userRequest.getPassword(),
                            authoritiesUser
                    )
            );

            int tokenVersion = userDetailService.tokenVersion(userRequest.getEmail());
            String token = jwtUtil.generateToken(userRequest.getEmail(), authoritiesUser,tokenVersion);
            String refreshToken = jwtUtil.generateRefreshToken(userRequest.getEmail(), authoritiesUser,tokenVersion);

            UserEntity user = userRepository.findByEmail(userRequest.getEmail()).get();

            return TokenResponse.builder().token(token).idUser(user.getId()).refreshToken(refreshToken).build();


        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid password");
        }
    }

    public TokenResponse refreshToken(TokenRefreshRequest refreshRequest){
        addBlackList(refreshRequest);
        int tokenVersion = jwtUtil.extractTokenVersion(refreshRequest.getToken());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<SimpleGrantedAuthority> authoritiesUser =  userDetailService.permissions(email);
        String tokenNew = jwtUtil.generateToken(email, authoritiesUser,tokenVersion);
        String refreshTokenNew = jwtUtil.generateRefreshToken(email, authoritiesUser,tokenVersion);
        return TokenResponse.builder().token(tokenNew).idUser(null).refreshToken(refreshTokenNew).build();
    }

    private void addBlackList(TokenRefreshRequest refreshRequest){
        Date exprDateToken = jwtUtil.extractExpiration(refreshRequest.getToken());
        Date exprDateRefresh = jwtUtil.extractExpiration(refreshRequest.getRefreshToken());
        RevokedToken revokedToken = new RevokedToken(refreshRequest.getToken(),exprDateToken);
        RevokedToken revokedRefresh = new RevokedToken(refreshRequest.getRefreshToken(),exprDateRefresh);
        revokedTokenService.addRevoked(revokedToken);
        revokedTokenService.addRevoked(revokedRefresh);
    }
}
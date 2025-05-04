package Controller10;

import Dto.Request.TokenRefreshRequest;
import Dto.Request.UserRequest;
import Dto.Response.TokenResponse;
import Service.AuthenticationService;
import Service.RevokedTokenService;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody UserRequest userRequest) {
        return  authenticationService.login(userRequest);
    }

    @PostMapping("/refreshToken")
    public  TokenResponse refreshToken(@RequestBody TokenRefreshRequest refreshRequest){
        return authenticationService.refreshToken(refreshRequest);
    }


}

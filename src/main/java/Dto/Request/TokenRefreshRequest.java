package Dto.Request;

import lombok.Getter;

@Getter
public class TokenRefreshRequest {
    private String token;
    private String refreshToken;
}

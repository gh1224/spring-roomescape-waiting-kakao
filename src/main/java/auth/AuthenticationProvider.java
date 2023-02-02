package auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;

    public TokenResponse authenticate(TokenRequest tokenRequest) {
        UserDetail userDetail = userDetailService.getUserDetailByUsername(tokenRequest.getUsername());
        checkAuthentication(userDetail, tokenRequest);

        String token = jwtTokenProvider.createToken(userDetail.getUsername(), userDetail.getRole());
        return new TokenResponse(token);
    }

    private void checkAuthentication(UserDetail userDetail, TokenRequest tokenRequest) {
        if (!userDetail.getPassword().equals(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }
}

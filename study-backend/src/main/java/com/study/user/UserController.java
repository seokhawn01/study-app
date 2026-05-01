package com.study.user;

import com.study.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ApiResponse<UserDto.Response> getMe(@RequestAttribute Long userId) {
    return ApiResponse.ok(userService.getUser(userId));
  }

  @PutMapping("/character")
  public ApiResponse<Void> updateCharacter(
      @RequestAttribute Long userId,
      @RequestBody UserDto.CharacterRequest request) {
    userService.updateCharacter(userId, request.getCharacterType());
    return ApiResponse.ok(null);
  }
}

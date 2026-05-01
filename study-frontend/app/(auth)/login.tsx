import { View, Text, TouchableOpacity, Alert } from "react-native";
import { useRouter } from "expo-router";
import * as WebBrowser from "expo-web-browser";
import * as Linking from "expo-linking";
import { saveTokens } from "../../lib/auth";

const KAKAO_CLIENT_ID =
  process.env.EXPO_PUBLIC_KAKAO_CLIENT_ID ?? "f34949a5f6fd7988369bda46068d0f91";
const GOOGLE_CLIENT_ID = process.env.EXPO_PUBLIC_GOOGLE_CLIENT_ID ?? "";
const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080";
const FRONTEND_REDIRECT_URI =
  process.env.EXPO_PUBLIC_FRONTEND_REDIRECT_URI ?? "studyapp://auth/callback";

export default function LoginScreen() {
  const router = useRouter();

  const handleKakaoLogin = async () => {
    const redirectUri = `${API_URL}/api/auth/kakao/callback`;
    const authUrl =
      `https://kauth.kakao.com/oauth/authorize` +
      `?client_id=${KAKAO_CLIENT_ID}` +
      `&redirect_uri=${encodeURIComponent(redirectUri)}` +
      `&response_type=code`;

    const result = await WebBrowser.openAuthSessionAsync(
      authUrl,
      FRONTEND_REDIRECT_URI
    );

    if (result.type === "success") {
      await processCallbackUrl(result.url);
    } else if (result.type === "cancel") {
      // 사용자가 취소한 경우 아무 처리 안 함
    }
  };

  const handleGoogleLogin = async () => {
    if (!GOOGLE_CLIENT_ID) {
      Alert.alert(
        "설정 필요",
        "Google 로그인 설정이 완료되지 않았습니다.\n관리자에게 문의하세요."
      );
      return;
    }
    const redirectUri = `${API_URL}/api/auth/google/callback`;
    const authUrl =
      `https://accounts.google.com/o/oauth2/v2/auth` +
      `?client_id=${GOOGLE_CLIENT_ID}` +
      `&redirect_uri=${encodeURIComponent(redirectUri)}` +
      `&response_type=code` +
      `&scope=openid%20email%20profile`;

    const result = await WebBrowser.openAuthSessionAsync(
      authUrl,
      FRONTEND_REDIRECT_URI
    );

    if (result.type === "success") {
      await processCallbackUrl(result.url);
    }
  };

  const processCallbackUrl = async (url: string) => {
    const parsed = Linking.parse(url);
    const params = parsed.queryParams as Record<string, string>;
    const { accessToken, refreshToken, isNewUser } = params;

    if (!accessToken || !refreshToken) {
      Alert.alert("로그인 실패", "다시 시도해주세요.");
      return;
    }

    await saveTokens(accessToken, refreshToken);
    router.replace("/(main)/home");
  };

  return (
    <View className="flex-1 bg-background items-center justify-center px-8">
      <Text className="text-3xl font-bold text-text mb-2">로그인</Text>
      <Text className="text-muted text-center mb-12">
        소셜 계정으로 간편하게 시작하세요
      </Text>

      <TouchableOpacity
        className="w-full bg-yellow-400 rounded-2xl py-4 items-center mb-4"
        onPress={handleKakaoLogin}
      >
        <Text className="text-text font-semibold text-base">
          카카오로 시작하기
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        className="w-full bg-white border border-gray-200 rounded-2xl py-4 items-center"
        onPress={handleGoogleLogin}
      >
        <Text className="text-text font-semibold text-base">
          구글로 시작하기
        </Text>
      </TouchableOpacity>
    </View>
  );
}

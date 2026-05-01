import { View, Text, TouchableOpacity } from "react-native";

export default function LoginScreen() {
  return (
    <View className="flex-1 bg-background items-center justify-center px-8">
      <Text className="text-3xl font-bold text-text mb-2">로그인</Text>
      <Text className="text-muted text-center mb-12">
        소셜 계정으로 간편하게 시작하세요
      </Text>

      <TouchableOpacity className="w-full bg-yellow-400 rounded-2xl py-4 items-center mb-4">
        <Text className="text-text font-semibold text-base">
          카카오로 시작하기
        </Text>
      </TouchableOpacity>

      <TouchableOpacity className="w-full bg-white border border-gray-200 rounded-2xl py-4 items-center">
        <Text className="text-text font-semibold text-base">
          구글로 시작하기
        </Text>
      </TouchableOpacity>
    </View>
  );
}

import { View, Text, TouchableOpacity } from "react-native";
import { useRouter } from "expo-router";
import AsyncStorage from "@react-native-async-storage/async-storage";

export default function OnboardingScreen() {
  const router = useRouter();

  async function handleStart() {
    await AsyncStorage.setItem("onboarded", "true");
    router.replace("/(auth)/login");
  }

  return (
    <View className="flex-1 bg-background items-center justify-center px-8">
      <Text className="text-4xl font-bold text-primary mb-4">StudyMate</Text>
      <Text className="text-lg text-muted text-center mb-2">
        나만의 공부 캐릭터를 찾고
      </Text>
      <Text className="text-lg text-muted text-center mb-12">
        매일 성장하세요
      </Text>
      <TouchableOpacity
        className="bg-primary rounded-2xl py-4 px-12"
        onPress={handleStart}
      >
        <Text className="text-white text-lg font-semibold">시작하기</Text>
      </TouchableOpacity>
    </View>
  );
}

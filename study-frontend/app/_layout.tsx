import "../global.css";
import { useEffect, useState } from "react";
import { Slot, useRouter, useSegments } from "expo-router";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import AsyncStorage from "@react-native-async-storage/async-storage";
import * as SecureStore from "expo-secure-store";

const queryClient = new QueryClient();

function RootLayoutNav() {
  const router = useRouter();
  const segments = useSegments();
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    checkAuthAndOnboarding();
  }, []);

  async function checkAuthAndOnboarding() {
    const token = await SecureStore.getItemAsync("accessToken");
    const onboarded = await AsyncStorage.getItem("onboarded");

    if (!onboarded) {
      router.replace("/(auth)/onboarding");
    } else if (!token) {
      router.replace("/(auth)/login");
    } else {
      router.replace("/(main)/home");
    }
    setIsReady(true);
  }

  if (!isReady) return null;
  return <Slot />;
}

export default function RootLayout() {
  return (
    <QueryClientProvider client={queryClient}>
      <RootLayoutNav />
    </QueryClientProvider>
  );
}

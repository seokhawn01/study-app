export type CharacterType = "OWL" | "TURTLE" | "LION" | "CAT" | "FOX";
export type Provider = "KAKAO" | "GOOGLE";

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
}

export interface User {
  id: number;
  email: string;
  nickname: string;
  characterType: CharacterType | null;
  createdAt: string;
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
  isNewUser: boolean;
}

export interface Mission {
  id: number;
  title: string;
  description: string;
  expReward: number;
  completed: boolean;
}

export interface Growth {
  level: number;
  exp: number;
  nextLevelExp: number;
}

export interface HomeData {
  character: CharacterType;
  todayMissions: Mission[];
  totalStudyMinutes: number;
  streak: number;
  growth: Growth;
}

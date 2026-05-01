import { CharacterType } from "@/types";

export const CHARACTER_INFO: Record<
  CharacterType,
  { name: string; description: string; studyTips: string[] }
> = {
  OWL: {
    name: "올빼미형",
    description: "밤에 가장 집중이 잘 되는 야행성 공부러",
    studyTips: ["늦은 밤 조용한 환경에서 집중하기", "낮잠으로 에너지 보충", "밤 10시 이후 핵심 과목 공부"],
  },
  TURTLE: {
    name: "거북이형",
    description: "느리지만 꾸준히, 매일 조금씩 성장하는 타입",
    studyTips: ["매일 30분이라도 반드시 공부", "작은 목표부터 달성하기", "공부 일지 작성으로 기록 남기기"],
  },
  LION: {
    name: "사자형",
    description: "목표를 정하면 거침없이 달려가는 도전형",
    studyTips: ["큰 목표를 세부 목표로 나누기", "타이머 활용 집중 공부", "경쟁 요소로 동기 부여"],
  },
  CAT: {
    name: "고양이형",
    description: "분위기와 환경에 민감한 감성파 공부러",
    studyTips: ["카페나 좋아하는 공간에서 공부", "음악으로 집중력 높이기", "기분 좋을 때 어려운 과목 도전"],
  },
  FOX: {
    name: "여우형",
    description: "철저한 계획으로 효율을 극대화하는 전략파",
    studyTips: ["전날 밤 다음날 계획 세우기", "우선순위에 따라 과목 배분", "주간 복습으로 완성도 높이기"],
  },
};

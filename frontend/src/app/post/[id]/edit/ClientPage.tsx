"use client";

import hotkeys from "hotkeys-js";
import { useEffect } from "react";
import type { components } from "@/lib/backend/apiV1/schema";

export default function ClientPage({
  post,
}: {
  post: components["schemas"]["PostDto"];
}) {
  useEffect(() => {
    hotkeys.filter = function (event) {
      return true; // 모든 엘리먼트에서 단축키가 동작하도록 설정
    };

    hotkeys("ctrl+s, command+s", function (event) {
      event.preventDefault();
      event.stopPropagation();
      console.log("저장 단축키가 눌렸습니다");
      return false;
    });

    return () => {
      hotkeys.unbind("ctrl+s, command+s");
    };
  }, []);

  return (
    <textarea
      className="flex-1 p-2 border"
      placeholder="저장은 Ctrl + S"
    ></textarea>
  );
}

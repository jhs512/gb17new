"use client";

import { useToast } from "@/hooks/use-toast";
import type { components } from "@/lib/backend/apiV1/schema";
import client from "@/lib/backend/client";
import dynamic from "next/dynamic";

const MonacoEditor = dynamic(() => import("./MonacoEditor"), {
  ssr: false,
  loading: () => <div>에디터 로딩중...</div>,
});

function parseConfig(content: string): {
  title: string;
  published: boolean;
  content: string;
} {
  // config 섹션이 있는지 확인
  if (content.startsWith("$$config")) {
    const configEndIndex = content.indexOf("$$", 2);

    if (configEndIndex === -1) {
      return { title: "", published: false, content: content.trim() };
    }

    const configSection = content.substring(8, configEndIndex);
    const mainContent = content.substring(configEndIndex + 4);

    // config 파싱
    const configLines = configSection.split("\n");
    const config: { [key: string]: any } = {};

    configLines.forEach((line) => {
      const [key, value] = line.split(": ").map((s) => s.trim());
      if (key === "published") {
        config[key] = value === "true";
      } else {
        config[key] = value;
      }
    });

    return {
      title: config.title!!.trim(),
      published: config.published!!,
      content: mainContent.trim(),
    };
  }

  // config 섹션이 없는 경우
  return {
    title: "",
    published: false,
    content: content.trim(),
  };
}

export default function ClientPage({
  post,
}: {
  post: components["schemas"]["PostWithContentDto"];
}) {
  const { toast } = useToast();

  const savePost = async (value: string) => {
    try {
      const { title, published, content } = parseConfig(value.trim());

      const res = await client.PUT("/api/v1/posts/{id}", {
        params: {
          path: {
            id: post.id,
          },
        },
        body: {
          title: title,
          content: content,
          published: published,
        },
      });

      if (!res.response.ok) {
        toast({
          title: "저장 실패",
          description: res.error?.msg,
        });
      } else {
        toast({
          title: "저장 완료",
          description: res.data?.msg,
        });
      }
    } catch (error) {
      toast({
        title: "저장 실패",
        description: "저장에 실패했습니다",
      });
    }
  };

  return (
    <div className="h-[calc(100vh-4rem)]">
      <MonacoEditor
        initialValue={`$$config
title: ${post.title}
published: ${post.published}
$$

${post.content || ""}`.trim()}
        onSave={savePost}
      />
    </div>
  );
}

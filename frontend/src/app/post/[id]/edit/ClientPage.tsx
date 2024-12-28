"use client";

import { useToast } from "@/hooks/use-toast";
import type { components } from "@/lib/backend/apiV1/schema";
import client from "@/lib/backend/client";
import hotkeys from "hotkeys-js";
import { useEffect, useRef } from "react";

interface Config {
  title: string;
  published: boolean;
}

function parseConfig(content: string): { config: Config; content: string } {
  const configMatch = content.match(/^\$\$config\n([\s\S]*?)\n\$\$([\s\S]*)$/);

  if (!configMatch) {
    return {
      config: {
        title: "",
        published: false,
      },
      content: content,
    };
  }

  const configStr = configMatch[1];
  const remainingContent = configMatch[2].trim();

  const config: Config = {
    title: "",
    published: false,
  };

  configStr.split("\n").forEach((line) => {
    const [key, value] = line.split(":").map((s) => s.trim());
    if (key === "title") {
      config.title = value;
    } else if (key === "published") {
      config.published = value.toLowerCase() === "true";
    }
  });

  return { config, content: remainingContent };
}

export default function ClientPage({
  post,
}: {
  post: components["schemas"]["PostWithContentDto"];
}) {
  const { toast } = useToast();
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const savePost = async () => {
    if (!textareaRef.current) return;

    try {
      const { config, content } = parseConfig(textareaRef.current.value);

      const res = await client.PUT("/api/v1/posts/{id}", {
        params: {
          path: {
            id: post.id,
          },
        },
        body: {
          title: config.title,
          content: content,
          published: config.published,
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
          description: "저장이 완료되었습니다",
        });
      }
    } catch (error) {
      toast({
        title: "저장 실패",
        description: "저장에 실패했습니다",
      });
    }
  };

  useEffect(() => {
    hotkeys.filter = function () {
      return true;
    };

    hotkeys("ctrl+s, command+s", function (event) {
      event.preventDefault();
      event.stopPropagation();
      savePost();
      return false;
    });

    return () => {
      hotkeys.unbind("ctrl+s, command+s");
    };
  }, []);

  const defaultValue = `$$config
title: ${post.title}
published: ${post.published}
$$

${post.content || ""}`.trim();

  return (
    <textarea
      ref={textareaRef}
      className="flex-1 p-2 border font-mono"
      placeholder="저장은 Ctrl + S"
      defaultValue={defaultValue}
    />
  );
}

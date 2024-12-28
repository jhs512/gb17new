"use client";

import client from "@/lib/backend/client";
import { useEffect, useRef } from "react";
import type { components } from "@/lib/backend/apiV1/schema";
import hotkeys from "hotkeys-js";
import type * as Monaco from "monaco-editor";

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
  const editorRef = useRef<HTMLDivElement>(null);
  const monacoRef = useRef<Monaco.editor.IStandaloneCodeEditor | undefined>(
    undefined
  );

  useEffect(() => {
    if (!editorRef.current) return;

    import("monaco-editor").then((monaco) => {
      monacoRef.current = monaco.editor.create(editorRef.current!, {
        value: `$$config
title: ${post.title}
published: ${post.published}
$$

${post.content || ""}`.trim(),
        language: "markdown",
        tabSize: 2,
        mouseWheelZoom: true,
        theme: "vs-dark",
        automaticLayout: true,
        minimap: {
          enabled: false,
        },
      });
    });

    return () => {
      monacoRef.current?.dispose();
    };
  }, [post]);

  const savePost = async () => {
    if (!monacoRef.current) return;

    try {
      const { config, content } = parseConfig(monacoRef.current.getValue());

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
        throw new Error("저장에 실패했습니다");
      }
    } catch (error) {
      console.error(error);
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

  return <div ref={editorRef} className="flex-1" />;
}

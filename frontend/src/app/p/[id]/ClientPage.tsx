"use client";

import MarkdownViewer from "@/components/MarkdownViewer";
import { components } from "@/lib/backend/apiV1/schema";
import { useEffect, useRef, useState } from "react";
import client from "@/lib/backend/client";
import { useToast } from "@/hooks/use-toast";

export default function ClientPage({
  post: initialPost,
}: {
  post: components["schemas"]["PostWithContentDto"];
}) {
  const [post, setPost] = useState(initialPost);
  const markdownViewerRef = useRef<any>(null);
  const { toast } = useToast();
  const lastModifyDateRef = useRef(post.modifyDate);

  useEffect(() => {
    let timeoutId: NodeJS.Timeout;

    const checkForUpdates = async () => {
      // 문서가 숨겨져 있으면 폴링 중지
      if (document.hidden) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(checkForUpdates, 10000);
        return;
      }

      const res = await client.GET("/api/v1/posts/{id}", {
        params: {
          path: {
            id: post.id,
          },
          query: {
            lastModifyDate: lastModifyDateRef.current,
          },
        },
      });

      if (res.response.status === 200 && res.data) {
        // 내용이 변경되었을 때
        lastModifyDateRef.current = res.data.modifyDate;

        // 마크다운 뷰어 내용 업데이트
        if (markdownViewerRef.current?.getInstance) {
          markdownViewerRef.current.getInstance().setMarkdown(res.data.content);
        }

        // 제목과 날짜 업데이트
        setPost((prev) => ({
          ...prev,
          title: res.data.title,
          modifyDate: res.data.modifyDate,
          content: res.data.content,
        }));

        toast({
          title: "문서 업데이트",
          description: "새로운 내용으로 업데이트되었습니다.",
        });
      }

      clearTimeout(timeoutId);
      timeoutId = setTimeout(checkForUpdates, 10000);
    };

    // 초기 폴링 시작
    timeoutId = setTimeout(checkForUpdates, 10000);

    // visibility change 이벤트 리스너
    const handleVisibilityChange = () => {
      if (!document.hidden) {
        checkForUpdates();
      }
    };
    document.addEventListener("visibilitychange", handleVisibilityChange);

    return () => {
      clearTimeout(timeoutId);
      document.removeEventListener("visibilitychange", handleVisibilityChange);
    };
  }, [post.id]);

  return (
    <div className="p-2">
      <h1 className="text-2xl font-bold">{post.title}</h1>
      <div className="text-sm text-muted-foreground mb-4">
        마지막 수정: {new Date(post.modifyDate).toLocaleString("ko-KR")}
      </div>
      <MarkdownViewer ref={markdownViewerRef} initialValue={post.content} />
    </div>
  );
}

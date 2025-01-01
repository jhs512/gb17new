import type { Metadata } from "next";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import ClientPage from "./ClientPage";

// meta
export const metadata: Metadata = {
  title: "Post",
  description: "",
};

export default async function Page({ params }: { params: { id: string } }) {
  const { id } = await params;
  const res = await client.GET("/api/v1/posts/{id}", {
    params: {
      path: {
        id: parseInt(id),
      },
    },
    headers: {
      cookie: (await cookies()).toString(),
    },
  });

  if (res.error) {
    return <div>{res.error.msg}</div>;
  }

  function processMarkdown(input: string) {
    // 1. $$??? 내용 $$ 패턴 제거
    input = input.replace(/\$\$[\s\S]*?\$\$/g, "");

    // 2. ``` 패턴으로 감싸진 코드 블록 제거
    input = input.replace(/```[\s\S]*?```/g, "");

    // 3. 특수 기호 #와 - 만 남기기
    const result = input.match(/[#-]/g) || []; // #와 -만 추출
    return result.join(""); // 배열을 문자열로 변환
  }

  const post = res.data!!;

  metadata.title = post.title;
  metadata.description = processMarkdown(post.content);

  return <ClientPage post={post} />;
}

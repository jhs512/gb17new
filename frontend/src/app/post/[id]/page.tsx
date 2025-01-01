import type { Metadata } from "next";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import ClientPage from "./ClientPage";

async function getPost(id: string) {
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
    return null;
  }

  return res.data!!;
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

export async function generateMetadata({
  params,
}: {
  params: { id: string };
}): Promise<Metadata> {
  const { id } = await params;
  const post = await getPost(id);

  if (!post) {
    return {
      title: "Error",
      description: "Post not found",
    };
  }

  return {
    title: post.title,
    description: processMarkdown(post.content),
  };
}

export default async function Page({ params }: { params: { id: string } }) {
  const { id } = await params;
  const post = await getPost(id);

  if (!post) {
    return <div>게시글을 찾을 수 없습니다.</div>;
  }

  return <ClientPage post={post} />;
}

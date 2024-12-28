"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import MarkdownViewer from "@/components/MarkdownViewer";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { MemberContext } from "@/stores/member";
import { use } from "react";
import { components } from "@/lib/backend/apiV1/schema";

export default function ClientPage({
  post,
}: {
  post: components["schemas"]["PostWithContentDto"];
}) {
  const { loginMember } = use(MemberContext);

  return (
    <main className="container mx-auto py-8 px-4">
      <div className="flex flex-col gap-6">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <h1 className="text-3xl font-bold tracking-tight">게시글 상세</h1>
            <p className="text-muted-foreground">게시글의 상세 내용입니다</p>
          </div>
          {loginMember?.id === post.authorId && (
            <Link href={`/post/${post.id}/edit`}>
              <Button>수정</Button>
            </Link>
          )}
        </div>

        <Separator />

        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="text-xl">{post.title}</CardTitle>
              <CardDescription>
                {new Date(post.createDate).toLocaleDateString("ko-KR", {
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                })}
              </CardDescription>
            </div>
          </CardHeader>
          <CardContent>
            <div className="flex flex-col gap-4">
              <div className="flex items-center gap-2 text-sm text-muted-foreground">
                <span>작성자: {post.authorName}</span>
                {!post.published && (
                  <span className="text-destructive">(비공개)</span>
                )}
              </div>
              {post.content && (
                <div className="prose prose-neutral dark:prose-invert max-w-none">
                  <MarkdownViewer initialValue={post.content} />
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { client } from "@/lib/backend/client";
import { cookies } from "next/headers";

export default async function PostDetail({
  params,
}: {
  params: { id: string };
}) {
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

  const post = res.data!!;

  return (
    <main className="container mx-auto py-8 px-4">
      <div className="flex flex-col gap-6">
        <div className="space-y-1">
          <h1 className="text-3xl font-bold tracking-tight">게시글 상세</h1>
          <p className="text-muted-foreground">게시글의 상세 내용입니다</p>
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
                <div className="prose max-w-none">
                  <p className="whitespace-pre-wrap">{post.content}</p>
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}

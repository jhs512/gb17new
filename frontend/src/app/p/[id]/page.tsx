import { Separator } from "@/components/ui/separator";
import { client } from "@/lib/backend/client";
import { PostCard } from "@/components/post-card";

export default async function PostDetail({ params }: { params: { id: string } }) {
  const id = params.id;
  const res = await client.GET("/api/v1/posts/{id}", {
    params: {
      path: {
        id: parseInt(id),
      },
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

        <PostCard
          title={post.title}
          createDate={post.createDate}
          authorName={post.authorName}
          published={post.published}
          content={post.content}
        />
      </div>
    </main>
  );
}

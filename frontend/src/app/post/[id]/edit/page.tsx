import client from "@/lib/backend/client";
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
    <textarea
      className="flex-1 p-2 border"
      placeholder="저장은 Ctrl + S"
    ></textarea>
  );
}

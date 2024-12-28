import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import ClientPage from "./ClientPage";

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

  if (res.error) {
    return <div>{res.error.msg}</div>;
  }

  const post = res.data!!;

  return <ClientPage post={post} />;
}

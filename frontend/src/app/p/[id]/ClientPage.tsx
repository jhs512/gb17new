"use client";

import MarkdownViewer from "@/components/MarkdownViewer";
import { components } from "@/lib/backend/apiV1/schema";

export default function ClientPage({
  post,
}: {
  post: components["schemas"]["PostWithContentDto"];
}) {
  return (
    <div className="p-2">
      <h1 className="text-2xl font-bold">{post.title}</h1>
      <MarkdownViewer initialValue={post.content} />
    </div>
  );
}

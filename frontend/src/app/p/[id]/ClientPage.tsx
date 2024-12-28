"use client";

import { Viewer } from "@/components/viewer";
import { components } from "@/lib/backend/apiV1/schema";

export default function ClientPage({
  post,
}: {
  post: components["schemas"]["PostWithContentDto"];
}) {
  return (
    <div className="p-2">
      <Viewer initialValue={post.content} />
    </div>
  );
}

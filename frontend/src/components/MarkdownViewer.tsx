"use client";

import dynamic from "next/dynamic";

const MarkdownViewerWrapper = dynamic(() => import("./MarkdownViewerWrapper"), {
  ssr: false,
  loading: () => <div>로딩중...</div>,
});

interface ViewerProps {
  initialValue: string;
}

export default function MarkdownViewer({ initialValue }: ViewerProps) {
  return <MarkdownViewerWrapper initialValue={initialValue} />;
}

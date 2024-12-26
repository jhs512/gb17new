"use client";

import dynamic from "next/dynamic";

const WrappedViewer = dynamic(() => import("./viewer-wrapper"), {
  ssr: false,
  loading: () => <div>로딩중...</div>,
});

interface ViewerProps {
  initialValue: string;
}

export function Viewer({ initialValue }: ViewerProps) {
  return <WrappedViewer initialValue={initialValue} />;
}

"use client";

import dynamic from "next/dynamic";
import { forwardRef } from "react";

const MarkdownViewerWrapper = dynamic(() => import("./MarkdownViewerWrapper"), {
  ssr: false,
  loading: () => <div>로딩중...</div>,
});

interface ViewerProps {
  initialValue: string;
}

const MarkdownViewer = forwardRef<any, ViewerProps>((props, ref) => {
  return <MarkdownViewerWrapper ref={ref} {...props} />;
});

MarkdownViewer.displayName = "MarkdownViewer";

export default MarkdownViewer;

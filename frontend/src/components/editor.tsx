"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/theme/toastui-editor-dark.css";
import "tui-color-picker/dist/tui-color-picker.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import "@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css";
import "prismjs/themes/prism.css";

import { Editor as TuiEditor } from "@toast-ui/react-editor";
import chart from "@toast-ui/editor-plugin-chart";
import codeSyntaxHighlight from "@toast-ui/editor-plugin-code-syntax-highlight";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";
import tableMergedCell from "@toast-ui/editor-plugin-table-merged-cell";
import uml from "@toast-ui/editor-plugin-uml";
import { forwardRef, useImperativeHandle, useRef } from "react";

interface EditorProps {
  initialValue?: string;
  placeholder?: string;
  height?: string;
  onChange?: (value: string) => void;
}

const Editor = forwardRef<TuiEditor, EditorProps>(
  ({ initialValue = "", placeholder, height = "600px", onChange }, ref) => {
    const editorRef = useRef<TuiEditor>(null);

    useImperativeHandle(ref, () => editorRef.current!, []);

    return (
      <TuiEditor
        ref={editorRef}
        initialValue={initialValue}
        placeholder={placeholder}
        previewStyle="vertical"
        height={height}
        initialEditType="markdown"
        useCommandShortcut={true}
        plugins={[
          chart,
          codeSyntaxHighlight,
          colorSyntax,
          tableMergedCell,
          uml,
        ]}
        onChange={() => {
          const instance = editorRef.current?.getInstance();
          onChange?.(instance?.getMarkdown() || "");
        }}
      />
    );
  }
);

Editor.displayName = "Editor";

export { Editor };

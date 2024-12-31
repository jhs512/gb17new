"use client";

import { useEffect, useRef } from "react";
import * as monaco from "monaco-editor";
import { useTheme } from "next-themes";

interface MonacoEditorProps {
  initialValue: string;
  onSave?: (value: string) => void;
}

export default function MonacoEditor({
  initialValue,
  onSave,
}: MonacoEditorProps) {
  const editorRef = useRef<monaco.editor.IStandaloneCodeEditor | null>(null);
  const monacoEl = useRef<HTMLDivElement>(null);
  const { theme } = useTheme();

  useEffect(() => {
    if (monacoEl.current) {
      editorRef.current = monaco.editor.create(monacoEl.current, {
        value: initialValue,
        language: "markdown",
        theme: "vs-dark",
        tabSize: 2,
        mouseWheelZoom: true,
        automaticLayout: true,
        minimap: { enabled: false },
        fontSize: 14,
        wordWrap: "on",
        lineNumbers: "on",
      });

      editorRef.current.addCommand(
        monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyS,
        () => {
          onSave?.(editorRef.current?.getValue() || "");
        }
      );

      return () => {
        editorRef.current?.dispose();
      };
    }
  }, [initialValue, theme]);

  return <div ref={monacoEl} className="h-full w-full" />;
}

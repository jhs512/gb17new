"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/chart/dist/toastui-chart.css";
import "@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css";
import "@toast-ui/editor-plugin-table-merged-cell/dist/toastui-editor-plugin-table-merged-cell.css";
import "prismjs/themes/prism.css";

import "@toast-ui/editor/dist/i18n/ko-kr";

import { Viewer } from "@toast-ui/react-editor";
import chart from "@toast-ui/editor-plugin-chart";
// @ts-ignore
import codeSyntaxHighlight from "@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight-all";
import tableMergedCell from "@toast-ui/editor-plugin-table-merged-cell";
import uml from "@toast-ui/editor-plugin-uml";

interface ViewerWrapperProps {
  initialValue: string;
}

// URL 파라미터 파싱 유틸리티 함수
function getUrlParams(url: string) {
  const params: { [key: string]: string } = {};
  if (url.includes("?")) {
    const queryString = url.split("?")[1];
    queryString.split("&").forEach((param) => {
      const [key, value] = param.split("=");
      params[key] = value;
    });
  }
  return params;
}

// 객체에서 특정 키만 필터링하는 유틸리티 함수
function filterObjectKeys(obj: { [key: string]: any }, allowedKeys: string[]) {
  return Object.keys(obj).reduce((filtered: { [key: string]: any }, key) => {
    if (allowedKeys.includes(key)) {
      filtered[key] = obj[key];
    }
    return filtered;
  }, {});
}

// 커스텀 플러그인들
function youtubePlugin() {
  const toHTMLRenderers = {
    youtube(node: any) {
      const html = renderYoutube(node.literal);

      return [
        { type: "openTag", tagName: "div", outerNewLine: true },
        { type: "html", content: html },
        { type: "closeTag", tagName: "div", outerNewLine: true },
      ];
    },
  };

  function renderYoutube(url: string) {
    url = url.replace("https://www.youtube.com/watch?v=", "");
    url = url.replace("http://www.youtube.com/watch?v=", "");
    url = url.replace("www.youtube.com/watch?v=", "");
    url = url.replace("youtube.com/watch?v=", "");
    url = url.replace("https://youtu.be/", "");
    url = url.replace("http://youtu.be/", "");
    url = url.replace("youtu.be/", "");

    let urlParams = getUrlParams(url);

    let width = "100%";
    let height = "100%";

    let maxWidth = "500";

    if (urlParams["max-width"]) {
      maxWidth = urlParams["max-width"];
    }

    let ratio = "aspect-[16/9]";

    let marginLeft = "auto";

    if (urlParams["margin-left"]) {
      marginLeft = urlParams["margin-left"];
    }

    let marginRight = "auto";

    if (urlParams["margin-right"]) {
      marginRight = urlParams["margin-right"];
    }

    let youtubeId = url;

    if (youtubeId.indexOf("?") !== -1) {
      let pos = url.indexOf("?");
      youtubeId = youtubeId.substring(0, pos);
    }

    return (
      '<div style="max-width:' +
      maxWidth +
      "px; margin-left:" +
      marginLeft +
      "; margin-right:" +
      marginRight +
      ';" class="' +
      ratio +
      ' relative"><iframe class="absolute top-0 left-0 w-full" width="' +
      width +
      '" height="' +
      height +
      '" src="https://www.youtube.com/embed/' +
      youtubeId +
      '" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></div>'
    );
  }

  return { toHTMLRenderers };
}

function codepenPlugin() {
  const toHTMLRenderers = {
    codepen(node: any) {
      const html = renderCodepen(node.literal);

      return [
        { type: "openTag", tagName: "div", outerNewLine: true },
        { type: "html", content: html },
        { type: "closeTag", tagName: "div", outerNewLine: true },
      ];
    },
  };

  function renderCodepen(url: string) {
    const urlParams = getUrlParams(url);

    let height = "400";

    if (urlParams.height) {
      height = urlParams.height;
    }

    let width = "100%";

    if (urlParams.width) {
      width = urlParams.width;
    }

    if (!width.includes("px") && !width.includes("%")) {
      width += "px";
    }

    let iframeUri = url;

    if (iframeUri.indexOf("#") !== -1) {
      let pos = iframeUri.indexOf("#");
      iframeUri = iframeUri.substring(0, pos);
    }

    return (
      '<iframe height="' +
      height +
      '" style="width: ' +
      width +
      ';" title="" src="' +
      iframeUri +
      '" allowtransparency="true" allowfullscreen="true"></iframe>'
    );
  }

  return { toHTMLRenderers };
}

function hidePlugin() {
  const toHTMLRenderers = {
    hide(node: any) {
      return [
        { type: "openTag", tagName: "div", outerNewLine: true },
        { type: "html", content: "" },
        { type: "closeTag", tagName: "div", outerNewLine: true },
      ];
    },
  };
  return { toHTMLRenderers };
}

function pptPlugin() {
  const toHTMLRenderers = {
    config(node: any) {
      return [
        { type: "openTag", tagName: "div", outerNewLine: true },
        { type: "html", content: "" },
        { type: "closeTag", tagName: "div", outerNewLine: true },
      ];
    },
  };
  return { toHTMLRenderers };
}

function configPlugin() {
  const toHTMLRenderers = {
    config(node: any) {
      return [
        { type: "openTag", tagName: "div", outerNewLine: true },
        { type: "html", content: "" },
        { type: "closeTag", tagName: "div", outerNewLine: true },
      ];
    },
  };
  return { toHTMLRenderers };
}

const MarkdownViewerWrapper = ({ initialValue }: ViewerWrapperProps) => {
  return (
    <Viewer
      initialValue={initialValue}
      language="ko-KR"
      plugins={[
        [
          chart,
          {
            minWidth: 100,
            maxWidth: 800,
            minHeight: 100,
            maxHeight: 400,
          },
        ],
        [codeSyntaxHighlight],
        tableMergedCell,
        [
          uml,
          {
            rendererURL: "http://www.plantuml.com/plantuml/svg/",
          },
        ],
        youtubePlugin,
        codepenPlugin,
        hidePlugin,
        configPlugin,
        pptPlugin,
      ]}
      linkAttributes={{ target: "_blank" }}
      customHTMLRenderer={{
        heading(node: any, { entering, getChildrenText }: any) {
          return {
            type: entering ? "openTag" : "closeTag",
            tagName: `h${node.level}`,
            attributes: {
              id: getChildrenText(node).trim().replaceAll(" ", "-"),
            },
          };
        },
        htmlBlock: {
          iframe(node: any) {
            const newAttrs = filterObjectKeys(node.attrs, [
              "src",
              "width",
              "height",
              "allow",
              "allowfullscreen",
              "frameborder",
              "scrolling",
            ]);

            return [
              {
                type: "openTag",
                tagName: "iframe",
                outerNewLine: true,
                attributes: newAttrs,
              },
              { type: "html", content: node.childrenHTML },
              { type: "closeTag", tagName: "iframe", outerNewLine: false },
            ];
          },
        },
      }}
    />
  );
};

export default MarkdownViewerWrapper;

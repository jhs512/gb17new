import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
  PaginationEllipsis,
} from "@/components/ui/pagination";
import { Separator } from "@/components/ui/separator";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import Link from "next/link";
import { Input } from "@/components/ui/input";
import { Search } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

function getPageNumbers(currentPage: number, totalPages: number) {
  const delta = 5;
  const range: number[] = [];
  const rangeWithDots: (number | string)[] = [];
  let l: number;

  range.push(1);

  for (let i = currentPage - delta; i <= currentPage + delta; i++) {
    if (i < totalPages && i > 1) {
      range.push(i);
    }
  }

  range.push(totalPages);

  for (let i of range) {
    if (l) {
      if (i - l === 2) {
        rangeWithDots.push(l + 1);
      } else if (i - l !== 1) {
        rangeWithDots.push("...");
      }
    }
    rangeWithDots.push(i);
    l = i;
  }

  return rangeWithDots;
}

function makeQueryString(params: {
  [key: string]: string | number | undefined;
}) {
  const searchParams = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined) {
      searchParams.set(key, String(value));
    }
  });
  return searchParams.toString();
}

export default async function Page({
  searchParams,
}: {
  searchParams: {
    page?: string;
    searchKeyword?: string;
    searchKeywordType?: "title" | "body";
  };
}) {
  const {
    page: _page,
    searchKeyword,
    searchKeywordType = "title",
  } = await searchParams;

  const page = Number(_page || 1);

  const res = await client.GET("/api/v1/posts/mine", {
    params: {
      query: {
        page,
        searchKeyword,
        searchKeywordType,
        pageSize: 10,
      },
    },
    headers: {
      cookie: (await cookies()).toString(),
    },
  });

  const resData = res.data!!;

  return (
    <main className="container mx-auto py-8 px-4">
      <div className="flex flex-col gap-6">
        <div className="space-y-1">
          <h1 className="text-3xl font-bold tracking-tight">내 글</h1>
          <p className="text-muted-foreground">
            총 {resData.totalItems || 0}개의 글이 있습니다
          </p>
        </div>

        <div className="relative w-full max-w-sm">
          <form className="flex gap-2">
            <Select name="searchKeywordType" defaultValue={searchKeywordType}>
              <SelectTrigger className="w-32">
                <SelectValue placeholder="검색 유형" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="title">제목</SelectItem>
                <SelectItem value="body">내용</SelectItem>
              </SelectContent>
            </Select>
            <Input
              placeholder="검색어를 입력하세요"
              name="searchKeyword"
              defaultValue={searchKeyword || ""}
              className="pl-8"
            />
          </form>
        </div>

        <Separator />

        <div className="grid gap-4">
          {resData.items.map((post) => (
            <Link key={post.id} href={`/post/${post.id}`}>
              <Card className="transition-colors hover:bg-accent">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <span className="text-muted-foreground">#{post.id}</span>
                      <CardTitle className="text-xl">{post.title}</CardTitle>
                    </div>
                    <CardDescription>
                      {new Date(post.createDate).toLocaleDateString("ko-KR", {
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                      })}
                    </CardDescription>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <span>작성자: {post.authorName}</span>
                    {!post.published && (
                      <span className="text-destructive">(비공개)</span>
                    )}
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>

        {resData.totalPages > 1 && (
          <Pagination>
            <PaginationContent>
              {page > 1 && (
                <PaginationItem>
                  <PaginationPrevious
                    href={`?${makeQueryString({
                      page: page - 1,
                      searchKeyword,
                      searchKeywordType,
                    })}`}
                  />
                </PaginationItem>
              )}

              {getPageNumbers(page, resData.totalPages).map((pageNum, idx) => (
                <PaginationItem key={idx}>
                  {pageNum === "..." ? (
                    <PaginationEllipsis />
                  ) : (
                    <PaginationLink
                      href={`?${makeQueryString({
                        page: pageNum,
                        searchKeyword,
                        searchKeywordType,
                      })}`}
                      isActive={pageNum === page}
                    >
                      {pageNum}
                    </PaginationLink>
                  )}
                </PaginationItem>
              ))}

              {page < resData.totalPages && (
                <PaginationItem>
                  <PaginationNext
                    href={`?${makeQueryString({
                      page: page + 1,
                      searchKeyword,
                      searchKeywordType,
                    })}`}
                  />
                </PaginationItem>
              )}
            </PaginationContent>
          </Pagination>
        )}
      </div>
    </main>
  );
}

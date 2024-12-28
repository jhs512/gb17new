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

export default async function Page({
  searchParams,
}: {
  searchParams: { page?: string; searchKeyword?: string };
}) {
  const { page: _page, searchKeyword } = await searchParams;

  const page = Number(_page || 1);

  const res = await client.GET("/api/v1/posts/mine", {
    params: {
      query: {
        page,
        searchKeyword,
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

        <Separator />

        <div className="grid gap-4">
          {resData.items.map((post) => (
            <Link key={post.id} href={`/post/${post.id}`}>
              <Card className="transition-colors hover:bg-accent">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-xl">{post.title}</CardTitle>
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
                  <PaginationPrevious href={`?page=${page - 1}`} />
                </PaginationItem>
              )}

              {getPageNumbers(page, resData.totalPages).map((pageNum, idx) => (
                <PaginationItem key={idx}>
                  {pageNum === "..." ? (
                    <PaginationEllipsis />
                  ) : (
                    <PaginationLink
                      href={`?page=${pageNum}`}
                      isActive={pageNum === page}
                    >
                      {pageNum}
                    </PaginationLink>
                  )}
                </PaginationItem>
              ))}

              {page < resData.totalPages && (
                <PaginationItem>
                  <PaginationNext href={`?page=${page + 1}`} />
                </PaginationItem>
              )}
            </PaginationContent>
          </Pagination>
        )}
      </div>
    </main>
  );
}

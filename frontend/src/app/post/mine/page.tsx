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
} from "@/components/ui/pagination";
import { Separator } from "@/components/ui/separator";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import Link from "next/link";

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

              {Array.from({ length: resData.totalPages }, (_, i) => i + 1).map(
                (_page) => (
                  <PaginationItem key={_page}>
                    <PaginationLink
                      href={`?page=${_page}`}
                      isActive={_page === page}
                    >
                      {_page}
                    </PaginationLink>
                  </PaginationItem>
                )
              )}

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

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
import { client } from "@/lib/backend/client";
import { cookies } from "next/headers";

export default async function Home({
  searchParams,
}: {
  searchParams: { page?: string; searchKeyword?: string };
}) {
  const { page, searchKeyword } = await searchParams;

  const currentPage = Number(page || 1);

  console.log({
    page: currentPage,
    searchKeyword,
    size: 10,
  });

  const res = await client.GET("/api/v1/posts", {
    query: {
      page: currentPage,
      searchKeyword,
      size: 10,
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
          <h1 className="text-3xl font-bold tracking-tight">게시판</h1>
          <p className="text-muted-foreground">
            총 {resData.totalItems || 0}개의 글이 있습니다
          </p>
        </div>

        <Separator />

        <div className="grid gap-4">
          {resData.items.map((post) => (
            <Card key={post.id}>
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
          ))}
        </div>

        {resData.totalPages > 1 && (
          <Pagination>
            <PaginationContent>
              {currentPage > 1 && (
                <PaginationItem>
                  <PaginationPrevious href={`?page=${currentPage - 1}`} />
                </PaginationItem>
              )}

              {Array.from({ length: resData.totalPages }, (_, i) => i + 1).map(
                (page) => (
                  <PaginationItem key={page}>
                    <PaginationLink
                      href={`?page=${page}`}
                      isActive={currentPage === page}
                    >
                      {page}
                    </PaginationLink>
                  </PaginationItem>
                )
              )}

              {currentPage < resData.totalPages && (
                <PaginationItem>
                  <PaginationNext href={`?page=${currentPage + 1}`} />
                </PaginationItem>
              )}
            </PaginationContent>
          </Pagination>
        )}
      </div>
    </main>
  );
}

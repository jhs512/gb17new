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
import { PostCard } from "@/components/post-card";

interface PageProps {
  searchParams: { page?: string };
}

export default async function Home({ searchParams }: PageProps) {
  const { page } = await searchParams;

  const currentPage = Math.max(1, Number(page ?? 1));

  const res = await client.GET("/api/v1/posts", {
    query: {
      page: currentPage,
      size: 10,
    },
  });

  const resData = res.data!!;

  const totalPages = resData.totalPages;

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
            <PostCard
              key={post.id}
              title={post.title}
              createDate={post.createDate}
              authorName={post.authorName}
              published={post.published}
            />
          ))}
        </div>

        {totalPages > 1 && (
          <Pagination>
            <PaginationContent>
              {currentPage > 1 && (
                <PaginationItem>
                  <PaginationPrevious href={`?page=${currentPage - 1}`} />
                </PaginationItem>
              )}

              {Array.from({ length: totalPages }, (_, i) => i + 1).map(
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

              {currentPage < totalPages && (
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

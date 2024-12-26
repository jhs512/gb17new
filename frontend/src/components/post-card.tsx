import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

interface PostCardProps {
  title: string;
  createDate: string;
  authorName: string;
  published: boolean;
  content?: string;
}

export function PostCard({
  title,
  createDate,
  authorName,
  published,
  content,
}: PostCardProps) {
  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="text-xl">{title}</CardTitle>
          <CardDescription>
            {new Date(createDate).toLocaleDateString("ko-KR", {
              year: "numeric",
              month: "long",
              day: "numeric",
            })}
          </CardDescription>
        </div>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col gap-4">
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <span>작성자: {authorName}</span>
            {!published && <span className="text-destructive">(비공개)</span>}
          </div>
          {content && (
            <div className="prose max-w-none">
              <p className="whitespace-pre-wrap">{content}</p>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
} 
"use client";

import Link from "next/link";

import client from "@/lib//backend/client";
import { MemberContext, useLoginMember } from "@/stores/member";
import { useEffect } from "react";
import { usePathname, useRouter } from "next/navigation";

export default function ClientLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const pathname = usePathname();
  const router = useRouter();

  const {
    setLoginMember,
    isLogin,
    loginMember,
    removeLoginMember,
    isLoginMemberPending,
  } = useLoginMember();

  const memberContextValue = {
    loginMember,
    setLoginMember,
    removeLoginMember,
    isLogin,
    isLoginMemberPending,
  };

  useEffect(() => {
    client.GET("/api/v1/members/me").then(({ data }) => {
      if (data) {
        setLoginMember(data.data);
      }
    });
  }, []);

  const logout = () => {
    client.DELETE("/api/v1/members/logout").then(({ error }) => {
      if (error) {
        alert(error.msg);
      } else {
        removeLoginMember();
      }
    });
  };

  const writePost = () => {
    client.POST("/api/v1/posts/temp").then(({ data }) => {
      if (data) {
        router.push(`/post/${data.data.id}/edit`);
      }
    });
  };

  return (
    <>
      <header className="p-2 flex items-center gap-4">
        {pathname !== "/" && (
          <button onClick={() => router.back()}>뒤로가기</button>
        )}
        <Link href="/">홈</Link>
        <Link href="/post/list">글</Link>
        {isLogin ? (
          <>
            <Link href="/post/mine">내 글</Link>
            <Link href={`/member/me`} className="flex items-center gap-2">
              {loginMember.name}님
              <img
                src={loginMember.profileImgUrl}
                width="40"
                className="rounded-full"
              />
            </Link>
            <button onClick={writePost}>글 쓰기</button>
            <button onClick={logout}>로그아웃</button>
          </>
        ) : (
          <>
            <a
              href={`${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/kakao?state=${process.env.NEXT_PUBLIC_FRONTEND_BASE_URL}`}
            >
              로그인
            </a>
          </>
        )}
      </header>
      <MemberContext.Provider value={memberContextValue}>
        <main className="flex-1 flex flex-col">{children}</main>
      </MemberContext.Provider>
      <footer className="p-2 text-center">제작자 연락처 : 010 8824 5558</footer>
    </>
  );
}

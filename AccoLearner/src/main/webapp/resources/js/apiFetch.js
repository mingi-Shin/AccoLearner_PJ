/**
 * 	전역 공통 함수 보관소 
 */

document.addEventListener('DOMContentLoaded', () => {
	console.log("전역 함수 온!");
});

/* ------------------------- 예 시 -------------------------------*/
/*

async function apiFetch(url, options = {}) {
  const token = localStorage.getItem("accessToken");
  const headers = { "Content-Type": "application/json", ...options.headers };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  let response = await fetch(url, { ...options, headers, credentials: "include" });

  if (response.status === 401) {
    const refreshRes = await fetch("/api/refresh", { method: "POST", credentials: "include" });
    if (refreshRes.ok) {
      const { accessToken } = await refreshRes.json();
      localStorage.setItem("accessToken", accessToken);
      headers["Authorization"] = `Bearer ${accessToken}`;
      response = await fetch(url, { ...options, headers, credentials: "include" });
    }
  }

  return response;
}


*/
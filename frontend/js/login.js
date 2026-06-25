async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!email || !password) {
        document.getElementById("message").innerText = "Vui lòng nhập email và mật khẩu";
        return;
    }

    try {
        const res = await fetch(API_URL + "/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        const result = await res.json();

        if (res.ok && result?.data?.access_token) {
            const token = result.data.access_token;
            localStorage.setItem("access_token", token);

            const profileRes = await fetch(API_URL + "/auth/me", {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!profileRes.ok) {
                throw new Error("Không lấy được thông tin người dùng");
            }

            const profileResult = await profileRes.json();
            const user = profileResult?.data;

            if (user) {
                localStorage.setItem("user", JSON.stringify(user));
            }

            window.location.href = "./daily_reports.html";
        } else {
            document.getElementById("message").innerText =
                result.message || result.detail || "Đăng nhập thất bại";
        }

    } catch (e) {
        console.error(e);
        document.getElementById("message").innerText =
            "Kiểm tra lại tài khoản và mật khẩu";
    }
}

document.getElementById("btnLogin").addEventListener("click", login);
// Initialize the page when document is ready
$(document).ready(function () {
    checkAuthAndGetUser();

    const storedUser = JSON.parse(localStorage.getItem("user") || "{}");
    if (storedUser.id) {
        $('#employee-id-input').val(storedUser.id);
    }

    loadAttendanceLogs(storedUser.id || null);

    $('#employee-id-input').on('keypress', function (e) {
        if (e.which === 13) {
            checkInByEmployeeId();
        }
    });
});

/**
 * Fetch attendance logs from the API for a specific employee ID
 */
function loadAttendanceLogs(employeeId = null) {
    const userData = JSON.parse(localStorage.getItem("user") || "{}");
    const targetEmployeeId = employeeId || userData.id || $('#employee-id-input').val().trim();
    const attendanceTableBody = $('#attendance-log-tbody');

    if (!targetEmployeeId) {
        attendanceTableBody.html('<tr><td colspan="6" class="empty-state-cell text-danger">Vui lòng nhập ID nhân viên</td></tr>');
        return;
    }

    attendanceTableBody.html('<tr><td colspan="6" class="empty-state-cell">Đang tải dữ liệu...</td></tr>');

    const currentDate = new Date();
    const day = currentDate.getDate();
    const month = currentDate.getMonth() + 1;
    const year = currentDate.getFullYear();

    $.ajax({
        url: `/attendance/logs/${targetEmployeeId}`,
        type: 'GET',
        data: {
            day: day,
            month: month,
            year: year
        },
        dataType: 'json',
        success: function (response) {
            if (response.status === 1000 && Array.isArray(response.data)) {
                displayAttendanceLogs(response.data);
            } else {
                attendanceTableBody.html('<tr><td colspan="6" class="empty-state-cell">Không có dữ liệu phù hợp.</td></tr>');
            }
        },
        error: function (xhr, status, error) {
            console.error("Lỗi lấy log chấm công:", error);
            attendanceTableBody.html('<tr><td colspan="6" class="empty-state-cell text-danger">Lỗi kết nối máy chủ hoặc API.</td></tr>');
        }
    });
}

/**
 * Render attendance logs into the table
 */
function displayAttendanceLogs(logs) {
    const attendanceTableBody = $('#attendance-log-tbody');

    if (!logs || !Array.isArray(logs) || logs.length === 0) {
        attendanceTableBody.html('<tr><td colspan="6" class="empty-state-cell">Chưa có dữ liệu chấm công.</td></tr>');
        return;
    }

    // Helper function to format time strings
    const formatTimeString = (timeStr) => {
        if (!timeStr) return "--:--";
        return timeStr.substring(0, 5);
    };

    // Generate HTML for table rows
    const tableRowsHtml = logs.map(logEntry => {
        return `
            <tr>
                <td>${logEntry.log_date}</td>
                <td>${formatTimeString(logEntry.shift_start)}</td>
                <td>${formatTimeString(logEntry.shift_end)}</td>
                <td class="text-primary fw-bold">${formatTimeString(logEntry.checked_time)}</td>
                <td>
                    <span class="badge bg-success-subtle text-success border border-success-subtle" style="font-size: 0.7rem;">
                        <i class="fa-solid fa-check-circle me-1"></i> Thành công
                    </span>
                </td>
            </tr>
        `;
    }).join('');

    attendanceTableBody.html(tableRowsHtml);
}

/**
 * Display a toast notification with the given message and type
 */
function displayToast(message, type = "success") {
    const toastElement = $('#liveToast');
    const toastMessage = $('#toast-message');

    // Remove existing background classes
    toastElement.removeClass('bg-success bg-danger bg-info');

    // Add appropriate background class based on type
    if (type === 'success') toastElement.addClass('bg-success');
    else if (type === 'danger') toastElement.addClass('bg-danger');
    else toastElement.addClass('bg-info');

    toastMessage.text(message);
    const toastInstance = new bootstrap.Toast(toastElement[0]);
    toastInstance.show();
}

/**
 * Submit an attendance check-in using the employee ID entered by the user.
 */
function checkInByEmployeeId() {
    const employeeId = $('#employee-id-input').val().trim();
    if (!employeeId) {
        displayToast("Vui lòng nhập ID nhân viên", "warning");
        return;
    }

    const button = $('#btn-check-in');
    const originalHtml = button.html();
    button.prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-2"></span> Đang xử lý...');

    $.ajax({
        url: '/attendance/manual',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ employee_id: employeeId }),
        success: function (response) {
            displayToast("Điểm danh thành công!", "success");
            loadAttendanceLogs(employeeId);
        },
        error: function (xhr) {
            let errorMessage = "Điểm danh thất bại!";
            if (xhr.responseJSON && xhr.responseJSON.detail) {
                errorMessage = xhr.responseJSON.detail;
            }
            displayToast(errorMessage, "danger");
        },
        complete: function () {
            button.prop('disabled', false).html(originalHtml);
        }
    });
}
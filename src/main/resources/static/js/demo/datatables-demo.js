$(document).ready(function() {
  var table = $('#dataTable').DataTable({
    "processing": true,
    "serverSide": true,
    "ajax": {
      "url": "/color",
      "type": "GET",
      "data": function(d) {},
      "dataSrc": function(json) {
        return json.data;
      }
    },
    "columns": [
      { "data": "id" },
      { "data": "code" },
      { "data": "name" },
      {
        "data": null,
        "render": function(data, type, row) {
          // Nội dung HTML của cột tiếp theo
          return `<button class="btn btn-info btn-circle-sm view-btn">
                                        <i class="fas fa-info-circle"></i>
                                    </button>`;
        }
      }
    ],
    "drawCallback": function(settings) {
      $('#dataTable tbody').on('click', '.view-btn', function() {
        let rowData = table.row($(this).closest('tr')).data();
        $.ajax({
          url: '/color/' + rowData.id,  // Đường dẫn API để lấy dữ liệu theo ID
          type: 'GET',
          success: function(response) {
            // Lấy dữ liệu từ response và hiển thị trên modal
            let data = response;

            $('#view-detail').modal('show');
            $('#view-detail .modal-body').text(data.name);
          },
          error: function(xhr, status, error) {
            // Xử lý lỗi nếu cần thiết
          }
        });

      });
    },
    searchDelay: 1500,
    "paging": true,
    "pageLength": 10,
    "lengthMenu": [10, 25, 50, 100],
  });
  $('#dataTable tbody').on('dblclick', 'tr', function() {
    // Lấy dữ liệu của dòng được click
    var rowData = table.row(this).data();

    $('#view-detail').modal('show');
    $('#view-detail .modal-body').text(rowData.name);
  });

});
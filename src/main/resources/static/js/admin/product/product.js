function converArrayToObject(dataForm){
  let jsonData = {};
  if (Array.isArray(dataForm)){
    dataForm.forEach((value, key) => {
      jsonData[key] = value;
    });
  }
  return jsonData;
}
function pullDataToForm(idForm,data){
  if (typeof data === 'object' && data !== null) {
    for (var key in data) {
      if (data.hasOwnProperty(key)) {
        $(`#${idForm} [name="${key}"]`).val(data[key]);
      }
    }
  }
}

function clearForm(idForm,data){
  if (typeof data === 'object' && data !== null) {
    for (var key in data) {
      if (data.hasOwnProperty(key)) {
        data[key]=""
      }
    }
    pullDataToForm(idForm,data)
  }
}




$(document).ready(function() {

  var urlBase = "/product";
  var objectName = "product"
  var tableName = "dataTable"

  var urlBaseDetail = "/product-detail";
  var objectNameDetail = "product-detail"
  var tableNameDetail = "tableChiTiet"






  var table = $(`#${tableName}`).DataTable({
    "processing": true,
    "serverSide": true,
    "ajax": {
      "url": urlBase,
      "type": "GET",
      "data": function(d) {},
      "dataSrc": function(json) {
        return json.data;
      }
    },
    "columns": [

      { "data": "code" },
      { "data": "name" },
      {
        "data": "brand.name",
        "render": function(data, type, row) {
          if (data){
            return data;
          }else {
            return "";
          }
        }
      },
      {
        "data": "category.name",
        "render": function(data, type, row) {
          if (data){
            return data;
          }else {
            return "";
          }
        }
      },
      {
        "data": "sole.name",
        "render": function(data, type, row) {
          if (data){
            return data;
          }else {
            return "";
          }
        }
      },
      {
        "data": "imageId",
        "orderable": false,
        "render": function(data, type, row) {
          if (data){
            return "<img src='/product/"+data+"/image' width='60px' height='80px'>"
          }else {
            return "null"
          }

        }
      },
      {
        "data": "type",
        "render": function(data, type, row) {
          if (data == 0) {
            return "Không hoạt động";
          } else if (data == 1) {
            return "Hoạt động";
          } else {
            return "";
          }
        }
      },

      {
        "data": null,
        "orderable": false,
        "render": function(data, type, row) {
          // Nội dung HTML của cột tiếp theo
          return `<button class="btn btn-info btn-circle-sm btn-view-update"><i class="fas fa-info-circle"></i></button>
                    <button class="btn btn-info btn btn-danger btn-circle-sm btn-delete"><i class="fas fa-trash"></i></button>`;
        }
      }
    ],
    "drawCallback": function(settings) {
      $(`#${tableName} tbody tr `).on('click', '.btn-view-update', loadDataModal);
      $(`#${tableName} tbody tr`).on('click', '.btn-delete', deleteDL);
    },
    searchDelay: 1500,
    "paging": true,
    "pageLength": 10,
    "lengthMenu": [10, 25, 50, 100],
  });


  $(`#${tableName}_filter input`).on('keypress', function(event) {
    // Kiểm tra mã phím
    if (event.keyCode === 13) {
      let searchValue = $(this).val();
      table.search(searchValue).draw();
    }
  });

  $(`#${tableNameDetail}_filter input`).on('keypress', function(event) {
    // Kiểm tra mã phím
    if (event.keyCode === 13) {
      let searchValue = $(this).val();
      table.search(searchValue).draw();
    }
  });




  // Show form update
  $(`#${tableName} tbody`).on('dblclick', 'tr', loadDataModal);
  $('#btn-view-add').click(function (){
    $('#view-add').modal('show');
  });



  function loadDataModal() {
    let rowData = table.row($(this).closest('tr')).data();
    $.ajax({
      url: urlBase+'/' + rowData.id,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        var data = response;

        $('#view-update').modal('show');
        pullDataToForm(`form-${objectName}-update`,data)

         for(let item of ["brand.id","category.id","sole.id"] ) {
          let bien = item.replace(".id","")
           if(data[bien] &&  $(`#form-${objectName}-update select[name="${item}"]`).find(`option[value="${data[bien]?.id}"]`).length == 0){
             var newOption = new Option(data[bien]?.name+"("+data[bien]?.code+")", data[bien]?.id, true, true);
             $(`#form-${objectName}-update select[name="${item}"]`).append(newOption);
           }
          $(`#form-${objectName}-update select[name="${item}"]`).val(data[bien]?.id).trigger('change')
        }
      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu")
      }
    });

  }


  // Sự kiện submit form Add
  $(`#form-${objectName}-add`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    ["brand.id","category.id","sole.id"].forEach((item)=>{
      let selectArray = formData.getAll(item);
      let selectValue = selectArray[0];
      formData.set(item, selectValue);
    })

    if ($(this).valid()) {
      $.ajax({
        url: urlBase,
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được thêm thành công!');
          table.ajax.reload(null, false);
          clearForm(`form-${objectName}-add`, response)
          $('#view-add').modal('hide');
        },
        error: function (xhr, status, error) {
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectName}-add`).validate().showErrors(errorResponse);
            }else if(xhr.responseText) {
              alert('Lỗi khi thêm dữ liệu: ' + xhr.responseText);
            }else {
              alert('Lỗi :' + error);
            }
          }else {
            alert('Lỗi :' + error);
          }
        }
      });
    }
  });




  // Sự kiện submit form Update
  $(`#form-${objectName}-update`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    ["brand.id","category.id","sole.id"].forEach((item)=>{
      let selectArray = formData.getAll(item);
      let selectValue = selectArray[0];
      formData.set(item, selectValue);
    })

    if ($(this).valid()) {

      $.ajax({
        url: urlBase+'/' + formData.get("id"),
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được cập nhật thành công!');
          table.ajax.reload(null, false);
          $('#view-update').modal('hide');
        },
        error: function (xhr, status, error) {
          // Xử lý lỗi nếu cần thiết
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectName}-update`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi cập nhật dữ liệu: ' + xhr.responseText);
            }
            // Hiển thị thông báo lỗi tương ứng với từng trường
          }else {
            alert('Lỗi :' + error);
          }

        }
      });
    }
  });

  //Sự kiện xóa
  function deleteDL() {
    let rowData = table.row($(this).closest('tr')).data();
    if (confirm("Xác nhận xóa")){
      $.ajax({
        url: urlBase+'/' + rowData.id,
        type: 'DELETE',
        success: function(response) {
          table.ajax.reload(null, false);
          alert("Xóa thành công")
        },
        error: function(xhr, status, error) {
          alert('Lỗi :' + xhr.responseText);
        }
      });
    }else {

    }
  }


  var configValidate = {
    rules: {
      code: {
        required: true,
        minlength: 3
      },
      name: {
        required: true
      },
      "brand.id": {
        required: true
      },
      "category.id": {
        required: true
      },
      "sole.id": {
        required: true
      },
      type: {
        required: true
      }
    },
    messages: {
      code: {
        required: "Vui lòng nhập trường này",
        minlength: "Trường này phải có ít nhất 3 ký tự"
      },
      name: {
        required: "Vui lòng nhập trường này"
      },
      "brand.id": {
        required: "Vui lòng nhập trường này"
      },
      "category.id": {
        required: "Vui lòng nhập trường này"
      },
      "sole.id": {
        required: "Vui lòng nhập trường này"
      },
      type: {
        required: "Vui lòng chọn trường này"
      }
    }
  }


// Validate form add
  $(`#form-${objectName}-add`).validate(configValidate);
// Validate form update
  $(`#form-${objectName}-update`).validate({
    rules:{...configValidate.rules,id:{required:true}},
    messages:{...configValidate.messages,id:{required:"Vui lòng nhập trường này"}}
  });





  var tableChiTiet = $(`#tableChiTiet`).DataTable({
    "processing": true,
    "serverSide": true,
    "ajax": {
      "url": urlBaseDetail,
      "type": "GET",
      "data": function(d) {
        var rowData = table.row('.selected-row').data();
        d.idProduct = rowData?.id
      },
      "dataSrc": function(json) {
        return json.data;
      }
    },
    "columns": [
      {
        "data": "color",
        "render": function(data, type, row) {
          if (data){
            return data.name+" | "+data.code;
          }else {
            return "";
          }
        }
      },
      {
        "data": "size",
        "render": function(data, type, row) {
          if (data){
            return  data.size+" | "+data.code;;
          }else {
            return "";
          }
        }
      }, {"data":"amount",
        "render": function (data, type, row, meta) {
          if (type === 'display') {
            return new Intl.NumberFormat('en-US').format(data);
          }
          return data;
      }
    },
      {"data":"price",
        "render": function (data, type, row, meta) {
          if (type === 'display') {
            return new Intl.NumberFormat('en-US').format(data);
          }
          return data;
        }
      },
      {
        "data": "type",
        "render": function(data, type, row) {
          if (data == 0) {
            return "Không hoạt động";
          } else if (data == 1) {
            return "Hoạt động";
          } else {
            return "";
          }
        }
      },
      {
        "data": null,
        "orderable": false,
        "render": function(data, type, row) {
          // Nội dung HTML của cột tiếp theo
          return `<button class="btn btn-info btn-circle-sm btn-view-update"><i class="fas fa-info-circle"></i></button>
                    <button class="btn btn-info btn btn-danger btn-circle-sm btn-delete"><i class="fas fa-trash"></i></button>`;
        }
      }
    ],
    "drawCallback": function(settings) {
      $(`#tableChiTiet tbody tr`).on('click', '.btn-view-update', loadDataModalDetail);
      $(`#tableChiTiet tbody tr`).on('click', '.btn-delete',deleteDLDetail);
    },
    searchDelay: 1500,
    "paging": true,
    "pageLength": 10,
    "lengthMenu": [10, 25, 50, 100],
  });

  // Show form update detail
  $(`#${tableNameDetail} tbody`).on('dblclick', 'tr', loadDataModalDetail);
  $('#btn-view-add-detail').click(function (){
    var dataProductSelected = table.row('.selected-row').data();
    if (!(dataProductSelected&&dataProductSelected.id)){
      alert("Chưa chọn sản phẩm")
    }else {
      $(`#form-product-detail-add input[name="product.id"]`).val(dataProductSelected.id)
      $('#view-detail-add').modal('show');
    }

  });

  function deleteDLDetail() {
    let rowData = tableChiTiet.row($(this).closest('tr')).data();
    if (confirm("Xác nhận xóa")){
      $.ajax({
        url: urlBaseDetail+'/' + rowData.id,
        type: 'DELETE',
        success: function(response) {
          tableChiTiet.ajax.reload(null, false);
          alert("Xóa thành công")
        },
        error: function(xhr, status, error) {
          alert('Lỗi :' + xhr.responseText);
        }
      });
    }else {

    }
  }

  // Sự kiện submit form add detail
  $(`#form-${objectNameDetail}-add`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    ["color.id","size.id"].forEach((item)=>{
      let selectArray = formData.getAll(item);
      let selectValue = selectArray[0];
      formData.set(item, selectValue);
    })

    if ($(this).valid()) {
      $.ajax({
        url: urlBaseDetail,
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được cập nhật thành công!');
          tableChiTiet.ajax.reload(null, false);
          clearForm(`form-${objectNameDetail}-add`, response)
          $('#view-detail-add').modal('hide');
        },
        error: function (xhr, status, error) {
          // Xử lý lỗi nếu cần thiết
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectNameDetail}-add`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi cập nhật dữ liệu: ' + xhr.responseText);
            }
            // Hiển thị thông báo lỗi tương ứng với từng trường
          }else {
            alert('Lỗi :' + error);
          }

        }
      });
    }
  });


  // Sự kiện submit form Update detail
  $(`#form-${objectNameDetail}-update`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    ["color.id","size.id"].forEach((item)=>{
      let selectArray = formData.getAll(item);
      let selectValue = selectArray[0];
      formData.set(item, selectValue);
    })

    if ($(this).valid()) {
      $.ajax({
        url: urlBaseDetail+'/' + formData.get("id"),
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được cập nhật thành công!');
          tableChiTiet.ajax.reload(null, false);
          $('#view-detail-update').modal('hide');
        },
        error: function (xhr, status, error) {
          // Xử lý lỗi nếu cần thiết
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectName}-update`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi cập nhật dữ liệu: ' + xhr.responseText);
            }
            // Hiển thị thông báo lỗi tương ứng với từng trường
          }else {
            alert('Lỗi :' + error);
          }

        }
      });
    }
  });



  function loadDataModalDetail() {
    var dataProductSelected = table.row('.selected-row').data();
    if (!(dataProductSelected&&dataProductSelected.id)){
      console.log("Chưa chọn sản phẩm")
    }
    let rowData = tableChiTiet.row($(this).closest('tr')).data();
    $.ajax({
      url: urlBaseDetail+'/' + rowData.id,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        var data = response;
        $('#view-detail-update').modal('show');
        pullDataToForm(`form-${objectNameDetail}-update`,data)
        $(`#form-${objectNameDetail}-update input[name='product.id']`).val(data.product.id)
        for(let item of ["color.id","size.id"] ) {
          let bien = item.replace(".id","")
          if(data[bien] &&  $(`#form-${objectNameDetail}-update select[name="${item}"]`).find(`option[value="${data[bien]?.id}"]`).length == 0){
            var newOption = new Option((data[bien].name||data[bien].size)+"("+data[bien]?.code+")", data[bien]?.id, true, true);
            $(`#form-${objectNameDetail}-update select[name="${item}"]`).append(newOption);
          }
          $(`#form-${objectNameDetail}-update select[name="${item}"]`).val(data[bien]?.id).trigger('change')
        }
      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu")
      }
    });

  }


  // chọn row
  $('#dataTable tbody').on('click', 'tr', function() {
    $('#dataTable tbody tr').removeClass('selected-row');
    $(this).addClass('selected-row');
    tableChiTiet.ajax.reload(null,false);
  });


  var selectedFiles = [];
  var inputFile = $('#fileInput').on('change', function() {
    var fileList = this.files;
    var imagePreview = $('#imagePreview');

    // Duyệt qua danh sách các tệp đã chọn
    for (var i = 0; i < fileList.length; i++) {
      let file = fileList[i];
      let reader = new FileReader();

      // Đọc và hiển thị ảnh lên giao diện
      reader.onload = function(event) {
        var image = $('<img>').attr('src', event.target.result);
        image.attr("width","150px");
        image.attr("height","200px");
        var deleteButton = $('<button>').text('x').addClass('delete-button');
        var imageContainer = $('<div>').addClass('image-container p-3 border-dark ').append(image, deleteButton);
        imagePreview.append(imageContainer);

        // Thêm sự kiện click cho nút xóa
        deleteButton.on('click', function() {
          $(this).parent().remove();
          selectedFiles.splice(selectedFiles.indexOf(file), 1);
          updateInputFileValue();
        });
      };

      reader.readAsDataURL(file);
    }
    for (var i = 0; i < fileList.length; i++) {
      let file = fileList[i];
      selectedFiles.push(file);
    }
    updateInputFileValue();
  });

  // Hàm cập nhật giá trị của input file với mảng selectedFiles
  function updateInputFileValue() {

    let fileList = new DataTransfer();
    // Thêm các tệp vào fileList
    for (var i = 0; i < selectedFiles.length; i++) {
      fileList.items.add(selectedFiles[i]);
    }
    // Gán lại giá trị của input file với fileList
    inputFile.prop('files', fileList.files)
  }




  $(`select[name*=".id"]`).each((index,element)=>{
    $(element).select2({
      maximumSelectionLength: 1,
      ajax: {
        url: '/'+element.name.replace(".id",""),
        type: 'GET',
        dataType: 'json',
        delay: 250,
        data: function(params) {
          return {
            "search[value]": params.term
          };
        },
        processResults: function(data) {

          return {
            results: data.data.map(item => {
              return {
                id: item.id,
                text: (item.name||item.size)+"("+item.code+")"
              };
            })
          };
        },
        cache: true
      }
    });
  })

  var configValidateDetail = {
    rules: {
      "product.id": {
        required: true,
      },
      amount: {
        required: true
      },
      price: {
        required: true
      },
      "color.id": {
        required: true
      },
      "size.id": {
        required: true
      },
      type: {
        required: true
      }
    },
    messages: {
      "product.id": {
        required: "Vui lòng nhập trường này",
      },
      amount: {
        required: "Vui lòng nhập trường này"
      },
      price: {
        required: "Vui lòng nhập trường này"
      },
      "color.id": {
        required: "Vui lòng nhập trường này"
      },
      "size.id": {
        required: "Vui lòng nhập trường này"
      },
      type: {
        required: "Vui lòng chọn trường này"
      }
    }
  }


// Validate form add
  $(`#form-${objectNameDetail}-add`).validate(configValidateDetail);
// Validate form update
  $(`#form-${objectNameDetail}-update`).validate({
    rules:{...configValidateDetail.rules,id:{required:true}},
    messages:{...configValidateDetail.messages,id:{required:"Vui lòng nhập trường này"}}
  });




});


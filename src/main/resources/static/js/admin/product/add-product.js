function converArrayToObject(dataForm){
  if (Array.isArray(dataForm))
    var dataFormObject = {}
  for (const data of dataForm) {
    dataFormObject[data.name]=data.value
  }
  return dataFormObject;
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
  $(`select[name*=".id"]`).each((index,element)=>{
    $(element).select2({
      maximumSelectionLength: (element.name=="size.id"||element.name=="color.id")?-1:1,
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
    }).on('change', function () {
      generateSelectedOptionsTable();
    });
  })
  const selectedVariants = {};

  function generateSelectedOptionsTable() {
    const selectedColors = $("select[name='color.id']").val() || [];
    const selectedSizes = $("select[name='size.id']").val() || [];
    const tableBody = $("#selected-options-table tbody");

    for (const colorId of selectedColors) {
      for (const sizeId of selectedSizes) {
        const variantId = `${colorId}_${sizeId}`;
        if (!selectedVariants[variantId]) {
          let row = $("<tr>").data("idSize", sizeId).data("idColor", colorId);
          row.attr("id", `row-${variantId}`);
          row.append($("<td>").text(getOptionText("color.id", colorId)));
          row.append($("<td>").text(getOptionText("size.id", sizeId)));
          row.append($("<td>").append($("<input>").attr("type", "number").addClass("form-control quantity-input")));
          row.append($("<td>").append($("<input>").attr("type", "number").addClass("form-control price-input")));
          const typeSelect = $("<select>").addClass("form-control").attr("name", "type-input");
          typeSelect.append($("<option>").val("1").prop("selected", true).text("Hoạt động"));
          typeSelect.append($("<option>").val("0").text("Không hoạt động"));
          row.append($("<td>").append(typeSelect));
          const deleteButton = $("<button>").addClass("btn btn-danger btn-sm delete-button-row-table").attr("type", "button").text("Xóa");
          deleteButton.data("variantId", variantId); // Lưu variantId trong data của button
          row.append($("<td>").append(deleteButton));
          tableBody.append(row);
          selectedVariants[variantId] = true;
        }
      }
    }

    for (const variantId in selectedVariants) {
      const [colorId, sizeId] = variantId.split("_");
      if (!selectedColors.includes(colorId) || !selectedSizes.includes(sizeId)) {
        $(`#row-${variantId}`).remove();
        delete selectedVariants[variantId];
      }
    }

  }
  //Đặt sự kiện kiểm tra khi giá trị của ô input thay đổi
  $(document).on('change', '.quantity-input', function() {
    // Lấy giá trị của ô input
    const inputValue = $(this).val();

    // Kiểm tra nếu giá trị là rỗng hoặc không phải là số nguyên dương
    if (inputValue === '' || !(/^\d+$/.test(inputValue) && parseInt(inputValue) > 0)) {
      alert('Vui lòng nhập giá trị hợp lệ.');
      // Xóa giá trị không hợp lệ
      $(this).val('');
    }
  });

  $(document).on('change', '.price-input', function() {
    // Lấy giá trị của ô input
    const inputValue = $(this).val();

    // Kiểm tra nếu giá trị là rỗng hoặc không phải là số nguyên dương
    if (inputValue === '' || !(/^\d+$/.test(inputValue) && parseInt(inputValue) > 0)) {
      alert('Vui lòng nhập giá trị hợp lệ.');
      // Xóa giá trị không hợp lệ
      $(this).val('');
    }
  });



  $(document).on("click", ".delete-button-row-table", function () {
    const variantId = $(this).data("variantId");
    $(`#row-${variantId}`).remove();
    delete selectedVariants[variantId];
  });

  function getDataFromTable() {

    let check = true;

    const data = [];

    $("#selected-options-table tbody tr").each(function () {
      const row = $(this);

      const amount = row.find(".quantity-input").val();
      const price = row.find(".price-input").val();
      if (amount==""||price==""){
        check=false;
      }
    })
    if (check){
      $("#selected-options-table tbody tr").each(function () {
        const row = $(this);
        const idColor = row.data("idColor");
        const idSize = row.data("idSize");
        const amount = row.find(".quantity-input").val();
        const price = row.find(".price-input").val();
        const type = row.find("select[name='type-input']").val();

        data.push({
          "color.id": idColor,
          "size.id": idSize,
          amount: amount,
          price: price,
          type: type
        });
      });
      return data;
    }else {
      return undefined;
    }


  }

  function getOptionText(selectName, optionId) {
    const select = $(`select[name='${selectName}']`);
    const option = select.find(`option[value='${optionId}']`);
    return option.text();
  }

  var selectedFiles = [];
  var inputFile = $('#fileInput').on('change', function() {

    loadfile(this,inputFile,selectedFiles,"imagePreview")

  });

  function loadfile(thiss,inputFilex,selectedFilesx,imagePreviewID){
    let fileList = thiss.files;
    let imagePreview = $(`#${imagePreviewID}`);
    for (var i = 0; i < fileList.length; i++) {
      let file = fileList[i];
      selectedFilesx.push(file);
    }
    updateInputFileValue(inputFilex,selectedFilesx);
    imagePreview.empty();

    // Duyệt qua danh sách các tệp đã chọn
    for (let i = 0; i < selectedFilesx.length; i++) {
      let file = selectedFilesx[i];
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
          selectedFilesx.splice(selectedFilesx.indexOf(file), 1);
          updateInputFileValue(inputFilex, selectedFilesx);
        });
      };

      reader.readAsDataURL(file);
    }

  }


  // Hàm cập nhật giá trị của input file với mảng selectedFiles
  function updateInputFileValue(inputFilex,selectedFilesx) {

    let fileList = new DataTransfer();
    // Thêm các tệp vào fileList
    for (let i = 0; i < selectedFilesx.length; i++) {
      fileList.items.add(selectedFilesx[i]);
    }

    if (inputFilex[0].files) {
      inputFilex[0].files = fileList.files; // Gán lại các tệp từ newFileList vào input file
    }
  }
  function appendDetailsData(formData, detailsArray, index) {
    var detail = detailsArray[index];
    for (var key in detail) {
      if (detail.hasOwnProperty(key)) {
        var fieldName = "details[" + index + "]." + key;
        formData.append(fieldName, detail[key]);
      }
    }
  }


  function getDataFromForm() {
    let formData = new FormData($("#form-product-add")[0]);
    formData.delete("type-input");
    formData.delete("color.id");
    formData.delete("size.id");
    let details = getDataFromTable()
    if (!details){
      $("#error-table").text("Nhập đủ thông tin trong bảng")
      return undefined;
    }else {
      $("#error-table").text("")
    }
    // Thêm dữ liệu từ mảng details vào FormData
    for (let i = 0; i < details.length; i++) {
      appendDetailsData(formData, details, i);
    }


    return formData;
  }



  $("#form-product-add").on('submit', function(e) {
      e.preventDefault();
      let data = getDataFromForm()
     if ($(this).valid()&&data) {

      $.ajax({
        url: "/product/add",
        type: 'POST',
        data: data,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được thêm thành công!');
          window.location.href = "/product/view";
        },
        error: function (xhr, status, error) {
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-product-add`).validate().showErrors(errorResponse);
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

  var configValidate = {
    rules: {
      code: {
        required: true,
        minlength: 3,
        noLeadingWhitespace: true, // Thêm quy tắc kiểm tra tùy chỉnh
        noTrailingWhitespace: true // Sử dụng quy tắc mới
      },
      name: {
        required: true,
        minlength: 4,
        noLeadingWhitespace: true ,
        noTrailingWhitespace: true // Sử dụng quy tắc mới
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
      },
      "color.id": {
        required: true
      },
      "size.id": {
        required: true
      }
    },
    messages: {
      code: {
        required: "Vui lòng nhập mã sản phẩm",
        minlength: "Mã sản phẩm phải có ít nhất 3 ký tự",
        noLeadingWhitespace: "Mã không được chứa khoảng trắng ở đầu dòng",
        noTrailingWhitespace: "Mã không được chứa khoảng trắng ở cuối dòng"
      },
      name: {
        required: "Vui lòng nhập tên sản phẩm",
        minlength: "Mã sản phẩm phải có ít nhất 4 ký tự",
        noLeadingWhitespace: "Tên không được chứa khoảng trắng ở đầu dòng",
        noTrailingWhitespace: "Tên không được chứa khoảng trắng ở cuối dòng"
      },
      "brand.id": {
        required: "Vui lòng chọn thương hiệu"
      },
      "category.id": {
        required: "Vui lòng chọn loại giày"
      },
      "sole.id": {
        required: "Vui lòng chọn đế giày"
      },
      type: {
        required: "Vui lòng chọn type"
      },
      "color.id": {
        required: "Vui lòng chọn màu sắc"
      },
      "size.id": {
        required: "Vui lòng chọn size"
      }
    }
  };

  $("#form-product-add").validate(configValidate);


  $(`button[id*="btn-view-add-"]`).each((index,element)=>{
    $(element).click(function (){
      $("#"+$(element).attr("id").replace("btn-","")).modal('show');
    })
  })

  // var configValidateFormAdd = {
  //   rules: {
  //     code: {
  //       required: true,
  //       minlength: 3
  //     },
  //     name: {
  //       required: true
  //     },
  //     size: {
  //       required: true
  //     },
  //     type: {
  //       required: true
  //     }
  //   },
  //   messages: {
  //     code: {
  //       required: "Vui lòng nhập trường này",
  //       minlength: "Trường này phải có ít nhất 3 ký tự"
  //     },
  //     name: {
  //       required: "Vui lòng nhập trường này"
  //     },
  //     size: {
  //       required: "Vui lòng nhập trường này"
  //     },
  //     type: {
  //       required: "Vui lòng chọn trường này"
  //     }
  //   }
  // }


  // Thêm quy tắc kiểm tra tùy chỉnh cho việc kiểm tra khoảng trắng ở đầu dòng
  $.validator.addMethod("noLeadingWhitespace", function(value) {
    return /^[^\s]+/.test(value);
  }, "Không được chứa khoảng trắng ở đầu dòng");

// Thêm quy tắc kiểm tra tùy chỉnh cho việc kiểm tra khoảng trắng ở cuối dòng
  $.validator.addMethod("noTrailingWhitespace", function(value) {
    // Kiểm tra xem giá trị sau khi trim có chứa khoảng trắng ở cuối dòng không
    return value === value.replace(/\s+$/, '');
  }, "Không được chứa khoảng trắng ở cuối dòng");




  $(`form[id*="form-add-"]`).each((index,element)=>{
    // Validate form add
    $(element).validate(configValidate);
    // Validate form update
    $(element).validate({
      rules:{...configValidate.rules,id:{required:true}},
      messages:{...configValidate.messages,id:{required:"Vui lòng nhập trường này"}}
    });

  })

  $(`form[id*="form-add-"]`).each((index,element)=>{
    $(element).on('submit', function(e) {
      e.preventDefault();
      if ($(this).valid()) {
        // Gửi AJAX request để cập nhật dữ liệu
        $.ajax({
          url: "/"+$(element).attr("id").replace("form-add-",""),
          type: 'POST',
          data: JSON.stringify(converArrayToObject($(this).serializeArray())),
          contentType: 'application/json',
          processData: true,
          success: function (response) {
            // Xử lý thành công
            alert('Dữ liệu đã được thêm thành công!');
            clearForm($(element).attr("id"), response)
            $('#view-add-'+$(element).attr("id").replace("form-add-","")).modal('hide');
          },
          error: function (xhr, status, error) {
            if(xhr.status==400){
              let errorResponse = xhr.responseJSON;
              if (errorResponse){
                $(this).validate().showErrors(errorResponse);
              }else {
                alert('Lỗi khi thêm dữ liệu: ' + xhr.responseText);
              }
            }else {
              alert('Lỗi :' + error);
            }
          }
        });
      }
    });
  })






})
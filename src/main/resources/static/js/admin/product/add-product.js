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
    console.log(getDataFromTable())
    getDataFromForm()
  }

  $(document).on("click", ".delete-button-row-table", function () {
    const variantId = $(this).data("variantId");
    $(`#row-${variantId}`).remove();
    delete selectedVariants[variantId];
  });



  function getDataFromTable() {
    const data = [];

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
  }

  function getOptionText(selectName, optionId) {
    const select = $(`select[name='${selectName}']`);
    const option = select.find(`option[value='${optionId}']`);
    return option.text();
  }

  var selectedFiles = [];
  var inputFile = $('#fileInput').on('change', function() {
    loadfile(this,selectedFiles,selectedFiles,"imagePreview")
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
    console.log(imagePreview)
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
    if (inputFilex.files) {
      inputFilex.files.clear(); // Xóa tất cả các tệp trong input file
      inputFilex.files = fileList.files; // Gán lại các tệp từ newFileList vào input file
    }
  }

  function getDataFromForm() {
    let formData = new FormData($("#form-product-add")[0]);
    formData.delete("quantity-input");
    formData.delete("price-input");
    formData.delete("type-input");
    formData.delete("color.id");
    formData.delete("size.id");

    formData.append("details", JSON.stringify(getDataFromTable()));


    return formData;
  }

  $("#form-product-add").on('submit', function(e) {
    e.preventDefault();
    let data = getDataFromForm()
    if ($(this).valid()) {
      $.ajax({
        url: "/product/add",
        type: 'POST',
        data: data,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được thêm thành công!');

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
})
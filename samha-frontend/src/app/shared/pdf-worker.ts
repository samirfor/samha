import html2canvas from "html2canvas";
import jsPDF from "jspdf";

addEventListener('message', ({ data }) => {
  const doc = new jsPDF();

  html2canvas(data.table, { scale: window.devicePixelRatio * 4 }).then(canvas => {
    const imgData = canvas.toDataURL('image/png');
    const imgProps = doc.getImageProperties(imgData);
    const pdfWidth = doc.internal.pageSize.getWidth();
    const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

    doc.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
    const pdfData = doc.output('blob');
    postMessage(pdfData);
  });

});

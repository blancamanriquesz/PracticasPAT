const API_URL = "http://localhost:8080/api/carritos";

document.addEventListener("DOMContentLoaded", () => {
  const pagina = window.location.pathname.split("/").pop();

  if (pagina === "productos.html") {
    inicializarPaginaProductos();
  }

  if (pagina === "carrito.html") {
    cargarCarrito();
  }
});

function inicializarPaginaProductos() {
  const tarjetas = document.querySelectorAll(".producto-card");

  tarjetas.forEach((tarjeta, index) => {
    const idArticulo = index + 1;
    const nombre = tarjeta.querySelector("h3")?.textContent.trim() || "Producto";

    const elementosTexto = tarjeta.querySelectorAll("p, span, h4, h5");
    let precioProducto = null;

    elementosTexto.forEach((el) => {
      const texto = el.textContent.trim();
      if (texto.includes("€") && precioProducto === null) {
        precioProducto = extraerPrecio(texto);
      }
    });

    if (precioProducto === null || isNaN(precioProducto)) {
      console.error("No se pudo obtener el precio de:", nombre);
      return;
    }

    if (tarjeta.querySelector(".btn-agregar")) return;

    const boton = document.createElement("button");
    boton.textContent = "Añadir al carrito";
    boton.className = "btn-agregar";
    boton.style.marginTop = "12px";
    boton.style.padding = "10px 16px";
    boton.style.border = "none";
    boton.style.borderRadius = "8px";
    boton.style.cursor = "pointer";
    boton.style.backgroundColor = "#1f1f1f";
    boton.style.color = "white";

    boton.addEventListener("click", async () => {
      try {
        const response = await fetch(API_URL, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            idArticulo: idArticulo,
            descripcion: nombre,
            unidades: 1,
            precioProducto: precioProducto
          })
        });

        if (!response.ok) {
          const errorTexto = await response.text();
          console.error("Error del servidor:", errorTexto);
          throw new Error(errorTexto || "No se pudo añadir el producto");
        }

        alert(`"${nombre}" se ha añadido al carrito`);
      } catch (error) {
        console.error("Error al añadir producto:", error);
        alert("Error al añadir el producto. Mira la consola F12.");
      }
    });

    const contenedor = tarjeta.querySelector(".contenido-card") || tarjeta;
    contenedor.appendChild(boton);
  });
}

async function cargarCarrito() {
  const bloqueResumen = document.querySelector(".bloque");

  if (!bloqueResumen) return;

  try {
    const response = await fetch(API_URL);

    if (!response.ok) {
      throw new Error("No se pudo obtener el carrito");
    }

    const carrito = await response.json();
    renderizarTablaCarrito(carrito, bloqueResumen);
  } catch (error) {
    console.error("Error al cargar carrito:", error);

    bloqueResumen.innerHTML = `
      <p class="mini-titulo">Resumen</p>
      <h2>Joyas seleccionadas</h2>
      <p style="color: red;">
        No se pudo cargar el carrito. Comprueba que la API está arrancada en http://localhost:8080
      </p>
    `;
  }
}

function renderizarTablaCarrito(carrito, contenedor) {
  let totalFinal = 0;
  let filas = "";

  if (carrito.length === 0) {
    filas = `
      <tr>
        <td colspan="5">El carrito está vacío.</td>
      </tr>
    `;
  } else {
    carrito.forEach((item) => {
      const unidades = item.unidades ?? 0;
      const precioProducto = item.precioProducto ?? 0;
      const precioFinal = item.precioFinal ?? 0;

      totalFinal += precioFinal;

      filas += `
        <tr>
          <td>${item.descripcion}</td>
          <td>${unidades}</td>
          <td>${formatearPrecio(precioProducto)}</td>
          <td>${formatearPrecio(precioFinal)}</td>
          <td>
            <button onclick="eliminarProducto(${item.idCarrito})" class="btn-eliminar">
              Eliminar
            </button>
          </td>
        </tr>
      `;
    });

    filas += `
      <tr class="fila-total">
        <td colspan="3">Total final</td>
        <td colspan="2">${formatearPrecio(totalFinal)}</td>
      </tr>
    `;
  }

  contenedor.innerHTML = `
    <p class="mini-titulo">Resumen</p>
    <h2>Joyas seleccionadas</h2>
    <table>
      <tr>
        <th>Producto</th>
        <th>Cantidad</th>
        <th>Precio unitario</th>
        <th>Total</th>
        <th>Acción</th>
      </tr>
      ${filas}
    </table>
  `;
}

async function eliminarProducto(idCarrito) {
  try {
    const response = await fetch(`${API_URL}/${idCarrito}`, {
      method: "DELETE"
    });

    if (!response.ok) {
      throw new Error("No se pudo eliminar el producto");
    }

    cargarCarrito();
  } catch (error) {
    console.error("Error al eliminar producto:", error);
    alert("No se pudo eliminar el producto del carrito.");
  }
}

function formatearPrecio(precio) {
  return `${Number(precio).toFixed(2).replace(".", ",")} €`;
}

function extraerPrecio(texto) {
  return Number(texto.replace("€", "").replace(",", ".").trim());
}
Drop database if exists DBTiendaMaven;
Create database DBTiendaMaven;
Use DBTiendaMaven;

-- ------------------- TABLAS ADICIONALES ------------------- 
Create table RegistroUsuarios(
	idUsuario int auto_increment,
    nombreRealUsuario varchar(50) not null,
    apellidoUsuario varchar(50) not null,
    nombreUsuario varchar(50) not null,
    contraseñaUsuario varchar(50) not null,
    constraint pk_usuarios primary key (idUsuario)
);

Create table RegistroAdministradores(
	idAdministrador int auto_increment,
    nombreRealAdministrador varchar(50) not null,
    apellidoAdministrador varchar(50) not null,
    nombreAdministrador varchar(50) not null,
    contraseñaAdministrador varchar(50) not null,
    constraint pk_administracion primary key (idAdministrador)
);
-- ------------------- TABLAS INDEPENDIENTES ------------------- 
Create table Productos(
	idProducto int auto_increment,
    nombreProducto varchar(50) not null,
    descripcionProducto varchar(100) not null,
    precioActual decimal(10,2) not null,
    precioAnterior decimal(10,2) not null,
    descuentoProducto varchar(100) not null,
    categoriaProducto varchar(100) not null,
    codigoBarra varchar(100) not null,
    ultimaActualizacion date not null,
	stock int not null,
    constraint pk_productos primary key (idProducto)
);

Create table Categorias(
	idCategoria int auto_increment,
    nombreCategoria varchar(50) not null,
    descripcionCategoria varchar(100) not null,
    tipoCategoria varchar(128) not null,
	constraint pk_categorias primary key (idCategoria)	
);

Create table Clientes(
	idCliente int auto_increment,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    telefonoCliente varchar(20) not null,
    direccionCliente varchar(255) not null,
    emailCliente varchar(100) not null,
    fechaRegistro date not null,
    constraint pk_clientes primary key (idCliente)
);

Create table Contacto(
	idContacto int auto_increment,
    nombre varchar(45) not null,
    email varchar(80) not null,
    mensaje varchar(125) not null,
    fechaEnvio date not null,
    constraint pk_contacto primary key (idContacto)
);
 
Create table Empresa(
	idEmpresa int auto_increment,
    nombre varchar(50) not null,
    direccion varchar(180) not null,
    telefono varchar(10) not null,
    horario varchar(25) not null,
    constraint pk_empresa primary key (idEmpresa)
);
-- ------------------- TABLAS DEPENDIENTES -------------------

Create table Pedidos(
	idPedido int auto_increment,
    idCliente int,
    fechaPedido datetime default current_timestamp,
    estadoPedido varchar (30) default 'Pendiente',
    estadoPago varchar (30) default 'Pendiente',
    metodoPago varchar(30) not null,
    tipoEntrega varchar(30) not null,
    total decimal(10,2),
    descuento decimal(10,2) default 0,
    tiempoEstimado int,
	constraint pk_pedidos primary key (idPedido),
    constraint fk_pedidos_clientes foreign key (idCliente)
				references Clientes(idCliente)
);

Create table DetallePedido(
	idDetalle int auto_increment,	
    idPedido int not null, -- Llave foranea
    idProducto int not null, -- Llave foranea
    cantidad int not null,
    precioUnitario decimal(10, 2) not null,
    constraint pk_detallepedido primary key (idDetalle),
    constraint fk_detallepedido_pedido foreign key (idPedido)
		references Pedidos(idPedido),
	constraint fk_detallepedido_producto foreign key (idProducto)
		references Productos(idProducto)
);
 
Create table Garantias(
	idGarantia int auto_increment,
    idProducto int not null, -- llave foranea
    duracion varchar(15) not null,
    constraint pk_garantias primary key (idGarantia),
    constraint fk_garantias_producto foreign key (idProducto)
		references Productos(idProducto)
);
 
create table Facturas (
    idFactura int auto_increment,
    idPedido int not null,
	idCliente int not null,
    fechaFactura date not null,
    totalFactura decimal(10,2) not null,
    impuestos decimal(10,2) default 0,
    metodoPago enum ('Tarjeta','Efectivo'),
    estadoFactura varchar(30) default 'Pendiente',
    numeroFactura varchar(50) not null,
	constraint pk_facturas primary key (idFactura),
	constraint fk_facturas_pedido foreign key (idPedido)
		references Pedidos(idPedido),
	constraint fk_facturas_clientes foreign key (idCliente)
		references Clientes(idCliente)        
	
);

-- ------------------ PROCEDIMIENTOS ALMACENADOS TABLAS INDEPENDIENTES -----------------

-- Tabla Productos

-- ---------------------------------- AGREGAR PRODUCTO ---------------------------------
delimiter //
	create procedure sp_agregarProducto(
		in p_nombreProducto varchar(50),
        in p_descripcionProducto varchar(100),
        in p_precioActual decimal(10,2),
        in p_precioAnterior decimal(10,2),
        in p_descuentoProducto varchar(100),
        in p_categoriaProducto varchar(100),
        in p_codigoBarra varchar(100),
        in p_ultimaActualizacion date,
        in p_stock int)
        begin
			insert into Productos(nombreProducto, descripcionProducto, precioActual, precioAnterior, descuentoProducto, categoriaProducto, codigoBarra, ultimaActualizacion, stock)
            values(p_nombreProducto, p_descripcionProducto, p_precioActual, p_precioAnterior, p_descuentoProducto, p_categoriaProducto, p_codigoBarra, p_ultimaActualizacion, p_stock);
		end//
delimiter ;

-- ---------------------------------- LISTAR PRODUCTOS ---------------------------------
delimiter //
	create procedure sp_listarProductos()
    begin
		select 
			P.idProducto as ID,
            P.nombreProducto as NOMBRE,
            P.descripcionProducto as DESCRIPCION,
            P.precioActual as PRECIO_ACTUAL,
            P.precioAnterior as PRECIO_ANTERIOR,
            P.descuentoProducto as DESCUENTO,
            P.categoriaProducto as CATEGORIA,
            P.codigoBarra as CODIGO_BARRA,
            P.ultimaActualizacion as ULTIMA_ACTUALIZACION,
            P.stock as STOCK
		from Productos P;
    end//
delimiter ;

-- ---------------------------------- EDITAR PRODUCTO ---------------------------------
delimiter //
	create procedure sp_editarProducto(
        in p_idProd int,
        in p_nombreProducto varchar(50),
        in p_descripcionProducto varchar(100),
        in p_precioActual decimal(10,2),
        in p_precioAnterior decimal(10,2),
        in p_descuentoProducto varchar(100),
        in p_categoriaProducto varchar(100),
        in p_codigoBarra varchar(100),
        in p_ultimaActualizacion date,
        in p_stock int)
		begin
			update Productos P
				set
					P.nombreProducto = p_nombreProducto,
                    P.descripcionProducto = p_descripcionProducto,
                    P.precioActual = p_precioActual,
                    P.precioAnterior = p_precioAnterior,
                    P.descuentoProducto = p_descuentoProducto,
                    P.categoriaProducto = p_categoriaProducto,
                    P.codigoBarra = p_codigoBarra,
                    P.ultimaActualizacion = p_ultimaActualizacion,
                    P.stock = p_stock
                where idProducto = p_idProd;
		end//
delimiter ;

-- ---------------------------------- ELIMINAR PRODUCTO ---------------------------------
delimiter //
	create procedure sp_eliminarProducto(in p_idProd int)
		begin
			delete from Productos where idProducto = p_idProd;
		end//
delimiter ;

-- ---------------------------------- BUSCAR PRODUCTO ---------------------------------
delimiter //
	create procedure sp_buscarProducto(in p_idProd int)
		begin
			select
				P.idProducto as ID,
				P.nombreProducto as NOMBRE,
				P.descripcionProducto as DESCRIPCION,
				P.precioActual as PRECIO_ACTUAL,
				P.precioAnterior as PRECIO_ANTERIOR,
				P.descuentoProducto as DESCUENTO,
				P.categoriaProducto as CATEGORIA,
				P.codigoBarra as CODIGO_BARRA,
				P.ultimaActualizacion as ULTIMA_ACTUALIZACION,
				P.stock as STOCK
				from Productos P
			where idProducto = p_idProd;
		end//
delimiter ;
-- -------- Registro Usuarios --------
delimiter //
create procedure sp_ListarUsuarios()
begin
	select
		u.idUsuario,
		u.nombreRealUsuario,
		u.apellidoUsuario,
		u.nombreUsuario,
		u.contraseñaUsuario
	from RegistroUsuarios u;
end//
delimiter ;

delimiter //
create procedure sp_AgregarUsuario(
	in nombreRealUsuario varchar(50),
	in apellidoUsuario varchar(50),
	in nombreUsuario varchar(50),
	in contraseñaUsuario varchar(50)
)
begin
	insert into RegistroUsuarios(
		nombreRealUsuario, apellidoUsuario, nombreUsuario, contraseñaUsuario
	)
	values (
		nombreRealUsuario, apellidoUsuario, nombreUsuario, contraseñaUsuario
	);
end//
delimiter ;

delimiter //
create procedure sp_BuscarUsuario(in idUsuario int)
begin
	select
		u.idUsuario,
		u.nombreRealUsuario,
		u.apellidoUsuario,
		u.nombreUsuario,
		u.contraseñaUsuario
	from RegistroUsuarios u
	where u.idUsuario = idUsuario;
end//
delimiter ;

delimiter //
create procedure sp_EditarUsuario(
	in idUsuario int,
	in nombreRealUsuario varchar(50),
	in apellidoUsuario varchar(50),
	in nombreUsuario varchar(50),
	in contraseñaUsuario varchar(50)
)
begin
	update RegistroUsuarios u
	set
		u.nombreRealUsuario = nombreRealUsuario,
		u.apellidoUsuario = apellidoUsuario,
		u.nombreUsuario = nombreUsuario,
		u.contraseñaUsuario = contraseñaUsuario
	where u.idUsuario = idUsuario;
end//
delimiter ;

delimiter //
create procedure spEliminarUsuario(IN idUsuario INT)
begin
	delete from RegistroUsuarios where idUsuario = idUsuario;
end//
delimiter ;

-- -------- Registro Administradores --------
delimiter //
create procedure sp_ListarAdministradores()
begin
	select
		a.idAdministrador,
		a.nombreRealAdministrador,
		a.apellidoAdministrador,
		a.nombreAdministrador,
		a.contraseñaAdministrador
	from RegistroAdministradores a;
end//
delimiter ;

delimiter //
create procedure sp_AgregarAdministrador(
	in nombreRealAdministrador varchar(50),
	in apellidoAdministrador varchar(50),
	in nombreAdministrador varchar(50),
	in contraseñaAdministrador varchar(50)
)
begin
	insert into RegistroAdministradores(
		nombreRealAdministrador, apellidoAdministrador, nombreAdministrador, contraseñaAdministrador
	)
	values (
		nombreRealAdministrador, apellidoAdministrador, nombreAdministrador, contraseñaAdministrador
	);
end//
delimiter ;

delimiter //
create procedure sp_BuscarAdministrador(in idAdministrador int)
begin
	select
		a.idAdministrador,
		a.nombreRealAdministrador,
		a.apellidoAdministrador,
		a.nombreAdministrador,
		a.contraseñaAdministrador
	from RegistroAdministradores a
	where a.idAdministrador = idAdministrador;
end//
delimiter ;

delimiter //
create procedure sp_EditarAdministrador(
	in idAdministrador int,
	in nombreRealAdministrador varchar(50),
	in apellidoAdministrador varchar(50),
	in nombreAdministrador varchar(50),
	in contraseñaAdministrador varchar(50)
)
begin
	update RegistroAdministradores a
	set
		a.nombreRealAdministrador = nombreRealAdministrador,
		a.apellidoAdministrador = apellidoAdministrador,
		a.nombreAdministrador = nombreAdministrador,
		a.contraseñaAdministrador = contraseñaAdministrador
	where a.idAdministrador = idAdministrador;
end//
delimiter ;

delimiter //
create procedure spEliminarAdministrador(IN idAdministrador INT)
begin
	delete from RegistroAdministradores where idAdministrador = idAdministrador;
end//
delimiter ;
-- ------------------------------- LLAMADA DE METODOS PRODUCTO -------------------------------

-- Listar
call sp_listarProductos();

-- Agregar (4 tuplas)
call sp_agregarProducto('Laptop HP', 'Laptop de 15 pulgadas, 8GB RAM, 256GB SSD', 599.99, 649.99, '10%', 'Electrónicos', '123456789012', '2023-05-10', 50);
call sp_agregarProducto('Smartphone Samsung', 'Teléfono inteligente con cámara de 48MP', 299.99, 349.99, '15%', 'Electrónicos', '987654321098', '2023-05-12', 100);
call sp_agregarProducto('Auriculares Bluetooth', 'Auriculares inalámbricos con cancelación de ruido', 89.99, 99.99, '10%', 'Accesorios', '456789123456', '2023-05-15', 75);
call sp_agregarProducto('Mouse Gamer', 'Mouse ergonómico con 6 botones programables', 49.99, 59.99, '20%', 'Accesorios', '789123456789', '2023-05-18', 120);

-- Actualizar
call sp_editarProducto(1, 'Laptop HP Pro', 'Laptop de 15 pulgadas, 16GB RAM, 512GB SSD', 699.99, 749.99, '10%', 'Electrónicos', '123456789012', '2023-05-20', 40);

-- Eliminar
-- call sp_eliminarProducto(4);

-- Buscar
call sp_buscarProducto(2);



-- Tabla Categorias

DELIMITER $$
CREATE PROCEDURE sp_venderProducto(IN pid INT, IN pcantidad INT)
BEGIN
    UPDATE Productos SET STOCK = STOCK - pcantidad WHERE idProducto = pid;
END $$
DELIMITER ;
-- ---------------------------------- AGREGAR CATEGORIA ---------------------------------
delimiter //
	create procedure sp_agregarCategoria(
		in p_nombreCategoria varchar(50),
        in p_descripcionCategoria varchar(100),
        in p_tipoCategoria varchar(128))
        begin
			insert into Categorias(nombreCategoria, descripcionCategoria, tipoCategoria)
            values(p_nombreCategoria, p_descripcionCategoria, p_tipoCategoria);
		end//
delimiter ;

-- ---------------------------------- LISTAR CATEGORIAS ---------------------------------
delimiter //
	create procedure sp_listarCategorias()
    begin
		select 
			C.idCategoria as ID,
            C.nombreCategoria as NOMBRE,
            C.descripcionCategoria as DESCRIPCION,
            C.tipoCategoria as TIPO
		from Categorias C;
    end//
delimiter ;

-- ---------------------------------- EDITAR CATEGORIA ---------------------------------
delimiter //
	create procedure sp_editarCategoria(
        in p_idCat int,
        in p_nombreCategoria varchar(50),
        in p_descripcionCategoria varchar(100),
        in p_tipoCategoria varchar(128))
		begin
			update Categorias C
				set
					C.nombreCategoria = p_nombreCategoria,
                    C.descripcionCategoria = p_descripcionCategoria,
                    C.tipoCategoria = p_tipoCategoria
                where idCategoria = p_idCat;
		end//
delimiter ;

-- ---------------------------------- ELIMINAR CATEGORIA ---------------------------------
delimiter //
	create procedure sp_eliminarCategoria(in p_idCat int)
		begin
			delete from Categorias where idCategoria = p_idCat;
		end//
delimiter ;

-- ---------------------------------- BUSCAR CATEGORIA ---------------------------------
delimiter //
	create procedure sp_buscarCategoria(in p_idCat int)
		begin
			select
				C.idCategoria as ID,
				C.nombreCategoria as NOMBRE,
				C.descripcionCategoria as DESCRIPCION,
				C.tipoCategoria as TIPO
				from Categorias C
			where idCategoria = p_idCat;
		end//
delimiter ;

-- ------------------------------- LLAMADA DE METODOS CATEGORIA -------------------------------

-- Listar
call sp_listarCategorias();

-- Agregar categorías 
call sp_agregarCategoria('Computadoras', 'Laptops, PCs de escritorio y componentes', 'Hardware');
call sp_agregarCategoria('Smartphones', 'Teléfonos inteligentes y accesorios', 'Dispositivos móviles');
call sp_agregarCategoria('Audio', 'Auriculares, altavoces y equipos de sonido', 'Audio tecnología');
call sp_agregarCategoria('Gaming', 'Consolas, periféricos y accesorios para gamers', 'Entretenimiento digital');

-- Actualizar una categoría
call sp_editarCategoria(1, 'Computadoras y Laptops', 'Equipos completos y componentes para computación', 'Hardware principal');

-- Eliminar 
-- call sp_eliminarCategoria(4);

-- Buscar 
call sp_buscarCategoria(2);


-- Tabla Clientes

-- ---------------------------------- AGREGAR CLIENTE ---------------------------------
delimiter //
	create procedure sp_agregarCliente(
		in p_nombreCliente varchar(50),
        in p_apellidoCliente varchar(50),
        in p_telefonoCliente varchar(20),
        in p_direccionCliente varchar(255),
        in p_emailCliente varchar(100),
        in p_fechaRegistro date)
        begin
			insert into Clientes(nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, emailCliente, fechaRegistro)
            values(p_nombreCliente, p_apellidoCliente, p_telefonoCliente, p_direccionCliente, p_emailCliente, p_fechaRegistro);
		end//
delimiter ;

-- ---------------------------------- LISTAR CLIENTES ---------------------------------
delimiter //
	create procedure sp_listarClientes()
    begin
		select 
			C.idCliente as ID,
            C.nombreCliente as NOMBRE,
            C.apellidoCliente as APELLIDO,
            C.telefonoCliente as TELEFONO,
            C.direccionCliente as DIRECCION,
            C.emailCliente as CORREO,
            C.fechaRegistro as FECHA_REGISTRO
		from Clientes C;
    end//
delimiter ;

-- ---------------------------------- EDITAR CLIENTE ---------------------------------
delimiter //
	create procedure sp_editarCliente(
        in p_idCliente int,
        in p_nombreCliente varchar(50),
        in p_apellidoCliente varchar(50),
        in p_telefonoCliente varchar(20),
        in p_direccionCliente varchar(255),
        in p_emailCliente varchar(100),
        in p_fechaRegistro date)
		begin
			update Clientes C
				set
					C.nombreCliente = p_nombreCliente,
                    C.apellidoCliente = p_apellidoCliente,
                    C.telefonoCliente = p_telefonoCliente,
                    C.direccionCliente = p_direccionCliente,
                    C.emailCliente = p_emailCliente,
                    C.fechaRegistro = p_fechaRegistro
                where idCliente = p_idCliente;
		end//
delimiter ;

-- ---------------------------------- ELIMINAR CLIENTE ---------------------------------
delimiter //
	create procedure sp_eliminarCliente(in p_idCl int)
		begin
			delete from Clientes where idCliente = p_idCl;
		end//
delimiter ;

-- ---------------------------------- BUSCAR CLIENTE ---------------------------------
delimiter //
	create procedure sp_buscarCliente(in p_idCl int)
		begin
			select
				C.idCliente as ID,
				C.nombreCliente as NOMBRE,
				C.apellidoCliente as APELLIDO,
				C.telefonoCliente as TELEFONO,
				C.direccionCliente as DIRECCION,
				C.emailCliente as CORREO,
				C.fechaRegistro as FECHA_REGISTRO
				from Clientes C
			where idCliente = p_idCl;
		end//
delimiter ;

-- ------------------------------- LLAMADA DE METODOS CLIENTE -------------------------------

-- Listar
call sp_listarClientes();

-- Agregar (4 tuplas con fechas explícitas)
call sp_agregarCliente('Juan', 'Pérez', '12345678', 'Calle 123, Zona 1', 'juan@email.com', '2023-01-15');
call sp_agregarCliente('María', 'Gómez', '87654321', 'Avenida 456, Zona 2', 'maria@email.com', '2023-02-20');
call sp_agregarCliente('Carlos', 'López', '55554444', 'Boulevard 789, Zona 3', 'carlos@email.com', '2023-03-10');
call sp_agregarCliente('Ana', 'Martínez', '11112222', 'Diagonal 101, Zona 4', 'ana@email.com', '2023-04-05');

-- Actualizar
call sp_editarCliente(1, 'Juan', 'Pérez Rodríguez', '12345678', 'Calle 123, Zona 1', 'juan.nuevo@email.com', '2023-01-15');

-- Eliminar
-- call sp_eliminarCliente(4);

-- Buscar
call sp_buscarCliente(1);



-- Tabla Contacto

-- ---------------------------------- AGREGAR CONTACTO ---------------------------------
delimiter //
	create procedure sp_agregarContacto(
		in p_nombre varchar(45),
		in p_email varchar(80),
		in p_mensaje varchar(125),
		in p_fechaEnvio date)
	begin
		insert into Contacto(nombre, email, mensaje, fechaEnvio)
		values(p_nombre, p_email, p_mensaje, p_fechaEnvio);
	end//
delimiter ;

-- ---------------------------------- LISTAR CONTACTOS ---------------------------------
delimiter //
	create procedure sp_listarContactos()
		begin
			select 
				C.idContacto as ID,
				C.nombre as NOMBRE,
				C.email as EMAIL,
				C.mensaje as MENSAJE,
				C.fechaEnvio as FECHA_ENVIO
			from Contacto C;
		end//
delimiter ;

-- ---------------------------------- EDITAR CONTACTO ---------------------------------
delimiter //
	create procedure sp_editarContacto(
		in p_idContacto int,
		in p_nombre varchar(45),
		in p_email varchar(80),
		in p_mensaje varchar(125),
		in p_fechaEnvio date)
	begin
		update Contacto C
    set
        C.nombre = p_nombre,
        C.email = p_email,
        C.mensaje = p_mensaje,
        C.fechaEnvio = p_fechaEnvio
    where idContacto = p_idContacto;
end//
delimiter ;

-- ---------------------------------- ELIMINAR CONTACTO ---------------------------------
delimiter //
	create procedure sp_eliminarContacto(in p_idContacto int)
		begin
			delete from Contacto where idContacto = p_idContacto;
		end//
delimiter ;

-- ---------------------------------- BUSCAR CONTACTO ---------------------------------
delimiter //
	create procedure sp_buscarContacto(in p_idContacto int)
		begin
			select
				C.idContacto as ID,
				C.nombre as NOMBRE,
				C.email as EMAIL,
				C.mensaje as MENSAJE,
				C.fechaEnvio as FECHA_ENVIO
			from Contacto C
			where idContacto = p_idContacto;
		end//
delimiter ;

-- ------------------------------- LLAMADA DE METODOS CONTACTO -------------------------------

-- Listar
call sp_listarContactos();

-- Agregar (4 tuplas con fechas explícitas)
call sp_agregarContacto('Carlos Méndez', 'carlos@email.com', 'Consulta sobre disponibilidad de productos', '2023-05-10');
call sp_agregarContacto('Ana López', 'ana@email.com', 'Solicitud de información técnica', '2023-05-12');
call sp_agregarContacto('Pedro Ramírez', 'pedro@email.com', 'Reclamo por garantía', '2023-05-15');
call sp_agregarContacto('María Gómez', 'maria@email.com', 'Felicitaciones por el servicio', '2023-05-18');

-- Actualizar
call sp_editarContacto(1, 'Carlos Méndez', 'carlos.nuevo@email.com', 'Consulta sobre disponibilidad y precios', '2023-05-10');

-- Eliminar
-- call sp_eliminarContacto(4);

-- Buscar
call sp_buscarContacto(1);



-- Tabla Empresa

-- ---------------------------------- AGREGAR EMPRESA ---------------------------------
delimiter //
create procedure sp_agregarEmpresa(
    in p_nombre varchar(50),
    in p_direccion varchar(180),
    in p_telefono varchar(10),
    in p_horario varchar(25))
begin
    insert into Empresa(nombre, direccion, telefono, horario)
    values(p_nombre, p_direccion, p_telefono, p_horario);
end//
delimiter ;

-- ---------------------------------- LISTAR EMPRESAS ---------------------------------
delimiter //
create procedure sp_listarEmpresas()
begin
    select 
        E.idEmpresa as ID,
        E.nombre as NOMBRE,
        E.direccion as DIRECCION,
        E.telefono as TELEFONO,
        E.horario as HORARIO
    from Empresa E;
end//
delimiter ;

-- ---------------------------------- EDITAR EMPRESA ---------------------------------
delimiter //
create procedure sp_editarEmpresa(
    in p_idEmpresa int,
    in p_nombre varchar(50),
    in p_direccion varchar(180),
    in p_telefono varchar(10),
    in p_horario varchar(25))
begin
    update Empresa E
    set
        E.nombre = p_nombre,
        E.direccion = p_direccion,
        E.telefono = p_telefono,
        E.horario = p_horario
    where idEmpresa = p_idEmpresa;
end//
delimiter ;

-- ---------------------------------- ELIMINAR EMPRESA ---------------------------------
delimiter //
create procedure sp_eliminarEmpresa(in p_idEmpresa int)
begin
    delete from Empresa where idEmpresa = p_idEmpresa;
end//
delimiter ;

-- ---------------------------------- BUSCAR EMPRESA ---------------------------------
delimiter //
create procedure sp_buscarEmpresa(in p_idEmpresa int)
begin
    select
        E.idEmpresa as ID,
        E.nombre as NOMBRE,
        E.direccion as DIRECCION,
        E.telefono as TELEFONO,
        E.horario as HORARIO
    from Empresa E
    where idEmpresa = p_idEmpresa;
end//
delimiter ;

-- ------------------------------- LLAMADA DE METODOS EMPRESA -------------------------------

-- Listar
call sp_listarEmpresas();

-- Agregar (ejemplos básicos)
call sp_agregarEmpresa('TecnoShop', 'Av. Tecnológica 123, Zona Industrial', '5551234567', 'Lunes-Viernes 9:00-18:00');
call sp_agregarEmpresa('ElectroMundo', 'Calle Circuito 456, Centro', '5557654321', 'Lunes-Sábado 8:00-20:00');

-- Actualizar
call sp_editarEmpresa(1, 'TecnoShop Plus', 'Av. Tecnológica 123-125, Zona Industrial', '5551234567', 'Lunes-Sábado 9:00-19:00');

-- Eliminar
-- call sp_eliminarEmpresa(2);

-- Buscar
call sp_buscarEmpresa(1);
-- ------------------ PROCEDIMIENTOS ALMACENADOS TABLAS DEPENDIENTES -----------------

-- Tabla Pedidos
-- ---------------------------------- AGREGAR PEDIDO ---------------------------------
delimiter //
create procedure sp_agregarPedido(
    in p_idCliente int,
    in p_metodoPago varchar(30),
    in p_tipoEntrega varchar(30),
    in p_total decimal(10,2),
    in p_descuento decimal(10,2),
    in p_tiempoEstimado int)
begin
    insert into Pedidos(idCliente, metodoPago, tipoEntrega, total, descuento, tiempoEstimado)
    values(p_idCliente, p_metodoPago, p_tipoEntrega, p_total, p_descuento, p_tiempoEstimado);
end//
delimiter ;

-- ---------------------------------- LISTAR PEDIDOS ---------------------------------
delimiter //
create procedure sp_listarPedidos()
begin
    select 
        P.idPedido as ID,
        P.idCliente as ID_CLIENTE,
        P.fechaPedido as FECHA_PEDIDO,
        P.estadoPedido as ESTADO_PEDIDO,
        P.estadoPago as ESTADO_PAGO,
        P.metodoPago as METODO_PAGO,
        P.tipoEntrega as TIPO_ENTREGA,
        P.total as TOTAL,
        P.descuento as DESCUENTO,
        P.tiempoEstimado as TIEMPO_ESTIMADO
    from Pedidos P;
end//
delimiter ;

-- ---------------------------------- EDITAR PEDIDO ---------------------------------
delimiter //
create procedure sp_editarPedido(
    in p_idPedido int,
    in p_idCliente int,
    in p_fechaPedido datetime,
    in p_estadoPedido varchar(30),
    in p_estadoPago varchar(30),
    in p_metodoPago varchar(30),
    in p_tipoEntrega varchar(30),
    in p_total decimal(10,2),
    in p_descuento decimal(10,2),
    in p_tiempoEstimado int)
begin
    update Pedidos P
    set
        P.idCliente = p_idCliente,
        P.fechaPedido = p_fechaPedido,
        P.estadoPedido = p_estadoPedido,
        P.estadoPago = p_estadoPago,
        P.metodoPago = p_metodoPago,
        P.tipoEntrega = p_tipoEntrega,
        P.total = p_total,
        P.descuento = p_descuento,
        P.tiempoEstimado = p_tiempoEstimado
    where idPedido = p_idPedido;
end//
delimiter ;

-- ---------------------------------- ELIMINAR PEDIDO ---------------------------------
delimiter //
create procedure sp_eliminarPedido(in p_idPedido int)
begin
    delete from Pedidos where idPedido = p_idPedido;
end//
delimiter ;

-- ---------------------------------- BUSCAR PEDIDO ---------------------------------
delimiter //
create procedure sp_buscarPedido(in p_idPedido int)
begin
    select
        P.idPedido as ID_PEDIDO,
        P.idCliente as ID_CLIENTE,
        P.fechaPedido as FECHA_PEDIDO,
        P.estadoPedido as ESTADO_PEDIDO,
        P.estadoPago as ESTADO_PAGO,
        P.metodoPago as METODO_PAGO,
        P.tipoEntrega as TIPO_ENTREGA,
        P.total as TOTAL,
        P.descuento as DESCUENTO,
        P.tiempoEstimado as TIEMPO_ESTIMADO
    from Pedidos P
    where idPedido = p_idPedido;
end//
delimiter ;

-- ---------------------------------- BUSCAR PEDIDOS POR CLIENTE ---------------------------------
delimiter //
create procedure sp_buscarPedidosPorCliente(in p_idCliente int)
begin
    select
        P.idPedido as ID_PEDIDO,
        P.fechaPedido as FECHA_PEDIDO,
        P.estadoPedido as ESTADO_PEDIDO,
        P.estadoPago as ESTADO_PAGO,
        P.metodoPago as METODO_PAGO,
        P.tipoEntrega as TIPO_ENTREGA,
        P.total as TOTAL,
        P.descuento as DESCUENTO
    from Pedidos P
    where idCliente = p_idCliente;
end//
delimiter ;

-- ------------------------------- LLAMADA DE METODOS PEDIDOS -------------------------------

-- Listar
call sp_listarPedidos();

-- Agregar (4 tuplas)
call sp_agregarPedido(1, 'Tarjeta de crédito', 'Domicilio', 599.99, 50.00, 30);
call sp_agregarPedido(2, 'Transferencia bancaria', 'Recoger en tienda', 299.99, 0.00, 15);
call sp_agregarPedido(3, 'Efectivo', 'Domicilio', 899.99, 100.00, 45);
call sp_agregarPedido(1, 'Tarjeta de débito', 'Domicilio', 1299.99, 150.00, 60);

-- Actualizar
call sp_editarPedido(1, 1, '2023-05-05', 'En proceso', 'Pagado', 'Tarjeta de crédito', 'Domicilio', 599.99, 50.00, 25);

-- Eliminar
-- call sp_eliminarPedido(4);

-- Buscar
call sp_buscarPedido(1);

-- Buscar pedidos por cliente
call sp_buscarPedidosPorCliente(1);



-- Tabla DetallePedido
-- ---------------------------------- AGREGAR DETALLE PEDIDO ---------------------------------
delimiter //
create procedure sp_agregarDetallePedido(
    in p_idPedido int,
    in p_idProducto int,
    in p_cantidad int,
    in p_precioUnitario decimal(10,2))
begin
    insert into DetallePedido(idPedido, idProducto, cantidad, precioUnitario)
    values(p_idPedido, p_idProducto, p_cantidad, p_precioUnitario);
end//
delimiter ;

-- ---------------------------------- LISTAR DETALLES PEDIDO ---------------------------------
delimiter //
create procedure sp_listarDetallesPedido()
begin
    select 
        DP.idDetalle as idDetalle,
        DP.idPedido as idPedido,
        DP.idProducto as idProducto,
        DP.cantidad as cantidad,
        DP.precioUnitario as precioUnitario,
        (DP.cantidad * DP.precioUnitario) as subtotal
    from DetallePedido DP;
end//
delimiter ;

-- ---------------------------------- EDITAR DETALLE PEDIDO ---------------------------------
delimiter //
create procedure sp_editarDetallePedido(
    in p_idDetalle int,
    in p_idPedido int,
    in p_idProducto int,
    in p_cantidad int,
    in p_precioUnitario decimal(10,2))
begin
    update DetallePedido DP
    set
        DP.idPedido = p_idPedido,
        DP.idProducto = p_idProducto,
        DP.cantidad = p_cantidad,
        DP.precioUnitario = p_precioUnitario
    where idDetalle = p_idDetalle;
end//
delimiter ;

-- ---------------------------------- ELIMINAR DETALLE PEDIDO ---------------------------------
delimiter //
create procedure sp_eliminarDetallePedido(in p_idDetalle int)
begin
    delete from DetallePedido where idDetalle = p_idDetalle;
end//
delimiter ;

-- ---------------------------------- BUSCAR DETALLE PEDIDO ---------------------------------
delimiter //
create procedure sp_buscarDetallePedido(in p_idDetalle int)
begin
    select
        DP.idDetalle as ID_DETALLE,
        DP.idPedido as ID_PEDIDO,
        DP.idProducto as ID_PRODUCTO,
        DP.cantidad as CANTIDAD,
        DP.precioUnitario as PRECIO_UNITARIO,
        (DP.cantidad * DP.precioUnitario) as SUBTOTAL
    from DetallePedido DP
    where idDetalle = p_idDetalle;
end//
delimiter ;

-- ---------------------------------- BUSCAR DETALLES POR PEDIDO ---------------------------------
delimiter //
create procedure sp_buscarDetallesPorPedido(in p_idPedido int)
begin
    select
        DP.idDetalle as ID_DETALLE,
        DP.idProducto as ID_PRODUCTO,
        P.nombreProducto as PRODUCTO,
        DP.cantidad as CANTIDAD,
        DP.precioUnitario as PRECIO_UNITARIO,
        (DP.cantidad * DP.precioUnitario) as SUBTOTAL
    from DetallePedido DP
    join Productos P on DP.idProducto = P.idProducto
    where DP.idPedido = p_idPedido;
end//
delimiter ;

-- ------------------------------- LLAMADA DE METODOS DETALLE PEDIDO -------------------------------

-- Listar
call sp_listarDetallesPedido();

-- Agregar (4 tuplas)
call sp_agregarDetallePedido(1, 1, 2, 599.99);  -- Pedido 1, Producto 1 (Laptop HP)
call sp_agregarDetallePedido(1, 3, 1, 89.99);   -- Pedido 1, Producto 3 (Auriculares)
call sp_agregarDetallePedido(2, 2, 1, 299.99);  -- Pedido 2, Producto 2 (Smartphone)
call sp_agregarDetallePedido(3, 4, 3, 49.99);   -- Pedido 3, Producto 4 (Mouse Gamer)

-- Actualizar
call sp_editarDetallePedido(1, 1, 1, 3, 599.99);  -- Cambiar cantidad de 2 a 3

-- Eliminar
-- call sp_eliminarDetallePedido(4);

-- Buscar
call sp_buscarDetallePedido(1);

-- Buscar detalles por pedido (muy útil para facturas)
call sp_buscarDetallesPorPedido(1);



-- Tabla Garantias

-- ---------------------------------- AGREGAR GARANTIA ---------------------------------
delimiter //
create procedure sp_agregarGarantia(
    in p_idProducto int,
    in p_duracion varchar(15))
begin
    insert into Garantias(idProducto, duracion)
    values(p_idProducto, p_duracion);
end//
delimiter ;

-- ---------------------------------- LISTAR GARANTIAS ---------------------------------
delimiter //
create procedure sp_listarGarantias()
begin
    select 
        G.idGarantia as ID_GARANTIA,
        G.idProducto as ID_PRODUCTO,
--       P.nombreProducto as PRODUCTO,
        G.duracion as DURACION
    from Garantias G
    join Productos P on G.idProducto = P.idProducto;
end//
delimiter ;

-- ---------------------------------- EDITAR GARANTIA ---------------------------------
delimiter //
create procedure sp_editarGarantia(
    in p_idGarantia int,
    in p_idProducto int,
    in p_duracion varchar(15))
begin
    update Garantias G
    set
        G.idProducto = p_idProducto,
        G.duracion = p_duracion
    where idGarantia = p_idGarantia;
end//
delimiter ;

-- ---------------------------------- ELIMINAR GARANTIA ---------------------------------
delimiter //
create procedure sp_eliminarGarantia(in p_idGarantia int)
begin
    delete from Garantias where idGarantia = p_idGarantia;
end//
delimiter ;

-- ---------------------------------- BUSCAR GARANTIA ---------------------------------
delimiter //
create procedure sp_buscarGarantia(in p_idGarantia int)
begin
    select
        G.idGarantia as ID_GARANTIA,
        G.idProducto as ID_PRODUCTO,
--        P.nombreProducto as PRODUCTO,
        G.duracion as DURACION
    from Garantias G
    where idGarantia = p_idGarantia;
end//
delimiter ;

-- Facturas
-- ---------------------------------- AGREGAR FACTURA ---------------------------------
delimiter //
create procedure sp_agregarFactura(
    in p_idPedido int,
    in p_fechaFactura date,
    in p_totalFactura decimal(10,2),
    in p_impuestos decimal(10,2),
    in p_metodoPago varchar(30),
    in p_estadoFactura varchar(30),
    in p_numeroFactura varchar(50),
    in p_idCliente int)
begin
    insert into Facturas (
        idPedido, fechaFactura, totalFactura, impuestos, metodoPago, estadoFactura, numeroFactura, idCliente
    )
    values (
        p_idPedido, p_fechaFactura, p_totalFactura, p_impuestos, p_metodoPago, p_estadoFactura, p_numeroFactura, p_idCliente
    );
end//
delimiter ;

-- ---------------------------------- LISTAR FACTURAS ---------------------------------
delimiter //
Create Procedure sp_listarFacturas()
begin
    select 
        f.idFactura,
        f.idPedido,
        f.idCliente,
        f.fechaFactura,
        f.totalFactura,
        f.impuestos,
        f.metodoPago,
        f.estadoFactura,
        f.numeroFactura,
        CONCAT(c.nombreCliente, ' ', c.apellidoCliente) as cliente
    from
        Facturas f
        join Clientes c on f.idCliente = c.idCliente;
end //
delimiter ;

-- ---------------------------------- EDITAR FACTURA ---------------------------------
delimiter //
create procedure sp_editarFactura(
    in p_idFactura int,
    in p_idPedido int,
    in p_fechaFactura date,
    in p_totalFactura decimal(10,2),
    in p_impuestos decimal(10,2),
    in p_metodoPago varchar(30),
    in p_estadoFactura varchar(30),
    in p_numeroFactura varchar(50),
    in p_idCliente int)
begin
    update Facturas f
    set
        f.idPedido = p_idPedido,
        f.fechaFactura = p_fechaFactura,
        f.totalFactura = p_totalFactura,
        f.impuestos = p_impuestos,
        f.metodoPago = p_metodoPago,
        f.estadoFactura = p_estadoFactura,
        f.numeroFactura = p_numeroFactura,
        f.idCliente = p_idCliente
    where f.idFactura = p_idFactura;
end//
delimiter ;

-- ---------------------------------- ELIMINAR FACTURA ---------------------------------
delimiter //
create procedure sp_eliminarFactura(in p_idFactura int)
begin
    delete from Facturas where idFactura = p_idFactura;
end//
delimiter ;

-- ---------------------------------- BUSCAR FACTURA ---------------------------------
delimiter //
create procedure sp_buscarFactura(in p_idFactura int)
begin
    select
        f.idFactura as idFactura,
        f.idPedido as idPedido,
        f.fechaFactura as fechaFactura,
        f.totalFactura as totalFactura,
        f.impuestos as impuestos,
        f.metodoPago as metodoPago,
        f.estadoFactura as estadoFactura,
        f.numeroFactura as numeroFactura,
        f.idCliente as idCliente
    from Facturas f
    where f.idFactura = p_idFactura;
end//
delimiter ;

-- ---------------------------------- BUSCAR FACTURAS POR CLIENTE ---------------------------------
delimiter //
create procedure sp_buscarFacturasPorCliente(in p_idCliente int)
begin
    select
        f.idFactura as idFactura,
        f.idPedido as idPedido,
        f.fechaFactura as fechaFactura,
        f.totalFactura as totalFactura,
        f.impuestos as impuestos,
        f.metodoPago as metodoPago,
        f.estadoFactura as estadoFactura,
        f.numeroFactura as numeroFactura
    from factura f
    where f.idCliente = p_idCliente;
end//
delimiter ;

-- ---------------------------------- BUSCAR GARANTIA POR PRODUCTO ---------------------------------
-- delimiter //
-- create procedure sp_buscarGarantiaPorProducto(in p_idProducto int)
-- begin
    -- select
       -- G.idGarantia as ID_GARANTIA,
        -- G.duracion as DURACION,
       -- P.nombreProducto as PRODUCTO,
       -- P.precioActual as PRECIO
    -- from Garantias G
    -- join Productos P on G.idProducto = P.idProducto
    -- where G.idProducto = p_idProducto;
-- end//
-- delimiter ;

-- ------------------------------- LLAMADA DE METODOS GARANTIAS -------------------------------

-- Listar
call sp_listarGarantias();

-- Agregar (4 tuplas)
call sp_agregarGarantia(1, '1 año');       -- Garantía para Laptop HP
call sp_agregarGarantia(2, '6 meses');     -- Garantía para Smartphone
call sp_agregarGarantia(3, '1 año');      -- Garantía para Auriculares
call sp_agregarGarantia(4, '2 años');     -- Garantía para Mouse Gamer

-- Actualizar
call sp_editarGarantia(1, 1, '2 años');   -- Extender garantía de Laptop

-- Eliminar
-- call sp_eliminarGarantia(4);

-- Buscar
call sp_buscarGarantia(1);

-- Buscar garantía por producto
-- call sp_buscarGarantiaPorProducto(1);


-- Administradores
call sp_AgregarAdministrador('Laura', 'Martínez', 'admin', 'admin1234');
call sp_AgregarAdministrador('Diego', 'López', 'a', '1');

-- Usuarios
call sp_AgregarUsuario('Diego','Lopez','1','1');

-- Facturas
call sp_agregarFactura(1, '2025-07-16', 150.50, 15.05, '1', 'Pagada', 'FAC-001', 1);
call sp_agregarFactura(2, '2025-07-17', 200.00, 20.00, '1', 'Pendiente', 'FAC-002', 2);
call sp_agregarFactura(3, '2025-07-18', 350.75, 35.08, '2', 'Pagada', 'FAC-003', 3);
call sp_agregarFactura(4, '2025-07-19', 120.00, 12.00, '1', 'Pendiente', 'FAC-004', 4);

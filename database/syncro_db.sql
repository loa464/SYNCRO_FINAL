--
-- PostgreSQL database dump
--

\restrict EMDEw5ocgZQcJ3pjrd2K1Y3FvD5aiNzhAYWJIlhuTZHO2SLZ7KslqXWt6XmmvF3

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: syncro; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA syncro;


ALTER SCHEMA syncro OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: auditoria; Type: TABLE; Schema: syncro; Owner: postgres
--

CREATE TABLE syncro.auditoria (
    id integer NOT NULL,
    evento character varying(50) NOT NULL,
    numero_cuenta character varying(20),
    monto numeric(15,2),
    descripcion text,
    fecha_hora timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE syncro.auditoria OWNER TO postgres;

--
-- Name: TABLE auditoria; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON TABLE syncro.auditoria IS 'Bitacora de auditoria del sistema.';


--
-- Name: auditoria_id_seq; Type: SEQUENCE; Schema: syncro; Owner: postgres
--

CREATE SEQUENCE syncro.auditoria_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE syncro.auditoria_id_seq OWNER TO postgres;

--
-- Name: auditoria_id_seq; Type: SEQUENCE OWNED BY; Schema: syncro; Owner: postgres
--

ALTER SEQUENCE syncro.auditoria_id_seq OWNED BY syncro.auditoria.id;


--
-- Name: clientes; Type: TABLE; Schema: syncro; Owner: postgres
--

CREATE TABLE syncro.clientes (
    dni character varying(20) NOT NULL,
    nombre character varying(150) NOT NULL,
    telefono character varying(30),
    correo character varying(120),
    direccion character varying(255),
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE syncro.clientes OWNER TO postgres;

--
-- Name: TABLE clientes; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON TABLE syncro.clientes IS 'Clientes registrados en el sistema bancario Syncro.';


--
-- Name: COLUMN clientes.dni; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON COLUMN syncro.clientes.dni IS 'Documento Nacional de Identidad. Clave de búsqueda principal.';


--
-- Name: configuracion_sistema; Type: TABLE; Schema: syncro; Owner: postgres
--

CREATE TABLE syncro.configuracion_sistema (
    id integer DEFAULT 1 NOT NULL,
    sobregiro_habilitado boolean DEFAULT true NOT NULL,
    seguro_anti_fraude boolean DEFAULT false NOT NULL,
    sms_global_activo boolean DEFAULT true NOT NULL,
    CONSTRAINT solo_una_fila CHECK ((id = 1))
);


ALTER TABLE syncro.configuracion_sistema OWNER TO postgres;

--
-- Name: TABLE configuracion_sistema; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON TABLE syncro.configuracion_sistema IS 'Parametros de configuracion global del sistema.';


--
-- Name: cuentas; Type: TABLE; Schema: syncro; Owner: postgres
--

CREATE TABLE syncro.cuentas (
    numero_cuenta character varying(20) NOT NULL,
    tipo_cuenta character varying(30) NOT NULL,
    saldo numeric(15,2) DEFAULT 0.00 NOT NULL,
    dni_dueno character varying(20) NOT NULL,
    activa boolean DEFAULT true NOT NULL,
    notificaciones_sms boolean DEFAULT false NOT NULL,
    moneda character varying(20) DEFAULT 'SOLES' NOT NULL,
    fecha_apertura timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT cuentas_tipo_cuenta_check CHECK (((tipo_cuenta)::text = ANY ((ARRAY['Ahorros'::character varying, 'Corriente'::character varying])::text[])))
);


ALTER TABLE syncro.cuentas OWNER TO postgres;

--
-- Name: TABLE cuentas; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON TABLE syncro.cuentas IS 'Cuentas bancarias asociadas a cada cliente.';


--
-- Name: COLUMN cuentas.tipo_cuenta; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON COLUMN syncro.cuentas.tipo_cuenta IS 'Tipo de cuenta: Ahorros (sin sobregiro) o Corriente (sobregiro -500, comisión $1.50).';


--
-- Name: notificaciones_sms; Type: TABLE; Schema: syncro; Owner: postgres
--

CREATE TABLE syncro.notificaciones_sms (
    id integer NOT NULL,
    mensaje text NOT NULL,
    enviado_a character varying(30),
    fecha_envio timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE syncro.notificaciones_sms OWNER TO postgres;

--
-- Name: TABLE notificaciones_sms; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON TABLE syncro.notificaciones_sms IS 'Registro historico de mensajes de texto despachados.';


--
-- Name: notificaciones_sms_id_seq; Type: SEQUENCE; Schema: syncro; Owner: postgres
--

CREATE SEQUENCE syncro.notificaciones_sms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE syncro.notificaciones_sms_id_seq OWNER TO postgres;

--
-- Name: notificaciones_sms_id_seq; Type: SEQUENCE OWNED BY; Schema: syncro; Owner: postgres
--

ALTER SEQUENCE syncro.notificaciones_sms_id_seq OWNED BY syncro.notificaciones_sms.id;


--
-- Name: transacciones; Type: TABLE; Schema: syncro; Owner: postgres
--

CREATE TABLE syncro.transacciones (
    id integer NOT NULL,
    tipo_operacion character varying(20) NOT NULL,
    monto numeric(15,2) NOT NULL,
    numero_cuenta character varying(20) NOT NULL,
    numero_cuenta_destino character varying(20),
    exitosa boolean DEFAULT true NOT NULL,
    descripcion text,
    fecha_hora timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT transacciones_monto_check CHECK ((monto > (0)::numeric)),
    CONSTRAINT transacciones_tipo_operacion_check CHECK (((tipo_operacion)::text = ANY ((ARRAY['DEPOSITO'::character varying, 'RETIRO'::character varying, 'TRANSFERENCIA'::character varying])::text[])))
);


ALTER TABLE syncro.transacciones OWNER TO postgres;

--
-- Name: TABLE transacciones; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON TABLE syncro.transacciones IS 'Historial de transacciones de cuentas.';


--
-- Name: COLUMN transacciones.numero_cuenta_destino; Type: COMMENT; Schema: syncro; Owner: postgres
--

COMMENT ON COLUMN syncro.transacciones.numero_cuenta_destino IS 'Solo se usa cuando el tipo_operacion es TRANSFERENCIA.';


--
-- Name: transacciones_id_seq; Type: SEQUENCE; Schema: syncro; Owner: postgres
--

CREATE SEQUENCE syncro.transacciones_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE syncro.transacciones_id_seq OWNER TO postgres;

--
-- Name: transacciones_id_seq; Type: SEQUENCE OWNED BY; Schema: syncro; Owner: postgres
--

ALTER SEQUENCE syncro.transacciones_id_seq OWNED BY syncro.transacciones.id;


--
-- Name: vista_capital_total; Type: VIEW; Schema: syncro; Owner: postgres
--

CREATE VIEW syncro.vista_capital_total AS
 SELECT count(*) AS total_cuentas,
    sum(saldo) AS capital_total,
    avg(saldo) AS saldo_promedio,
    max(saldo) AS saldo_maximo,
    min(saldo) AS saldo_minimo
   FROM syncro.cuentas
  WHERE (activa = true);


ALTER VIEW syncro.vista_capital_total OWNER TO postgres;

--
-- Name: vista_clientes_cuentas; Type: VIEW; Schema: syncro; Owner: postgres
--

CREATE VIEW syncro.vista_clientes_cuentas AS
 SELECT c.dni,
    c.nombre,
    c.telefono,
    ct.numero_cuenta,
    ct.tipo_cuenta,
    ct.saldo,
    ct.activa,
    ct.notificaciones_sms
   FROM (syncro.clientes c
     LEFT JOIN syncro.cuentas ct ON (((c.dni)::text = (ct.dni_dueno)::text)))
  ORDER BY c.nombre;


ALTER VIEW syncro.vista_clientes_cuentas OWNER TO postgres;

--
-- Name: vista_ultimas_transacciones; Type: VIEW; Schema: syncro; Owner: postgres
--

CREATE VIEW syncro.vista_ultimas_transacciones AS
 SELECT t.id,
    t.tipo_operacion,
    t.monto,
    t.numero_cuenta,
    t.numero_cuenta_destino,
    t.exitosa,
    t.descripcion,
    t.fecha_hora,
    c.nombre AS nombre_cliente
   FROM ((syncro.transacciones t
     JOIN syncro.cuentas ct ON (((t.numero_cuenta)::text = (ct.numero_cuenta)::text)))
     JOIN syncro.clientes c ON (((ct.dni_dueno)::text = (c.dni)::text)))
  ORDER BY t.fecha_hora DESC
 LIMIT 10;


ALTER VIEW syncro.vista_ultimas_transacciones OWNER TO postgres;

--
-- Name: auditoria id; Type: DEFAULT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.auditoria ALTER COLUMN id SET DEFAULT nextval('syncro.auditoria_id_seq'::regclass);


--
-- Name: notificaciones_sms id; Type: DEFAULT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.notificaciones_sms ALTER COLUMN id SET DEFAULT nextval('syncro.notificaciones_sms_id_seq'::regclass);


--
-- Name: transacciones id; Type: DEFAULT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.transacciones ALTER COLUMN id SET DEFAULT nextval('syncro.transacciones_id_seq'::regclass);


--
-- Name: auditoria auditoria_pkey; Type: CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.auditoria
    ADD CONSTRAINT auditoria_pkey PRIMARY KEY (id);


--
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (dni);


--
-- Name: configuracion_sistema configuracion_sistema_pkey; Type: CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.configuracion_sistema
    ADD CONSTRAINT configuracion_sistema_pkey PRIMARY KEY (id);


--
-- Name: cuentas cuentas_pkey; Type: CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.cuentas
    ADD CONSTRAINT cuentas_pkey PRIMARY KEY (numero_cuenta);


--
-- Name: notificaciones_sms notificaciones_sms_pkey; Type: CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.notificaciones_sms
    ADD CONSTRAINT notificaciones_sms_pkey PRIMARY KEY (id);


--
-- Name: transacciones transacciones_pkey; Type: CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.transacciones
    ADD CONSTRAINT transacciones_pkey PRIMARY KEY (id);


--
-- Name: idx_auditoria_fecha; Type: INDEX; Schema: syncro; Owner: postgres
--

CREATE INDEX idx_auditoria_fecha ON syncro.auditoria USING btree (fecha_hora);


--
-- Name: idx_cuentas_dni; Type: INDEX; Schema: syncro; Owner: postgres
--

CREATE INDEX idx_cuentas_dni ON syncro.cuentas USING btree (dni_dueno);


--
-- Name: idx_trans_cuenta; Type: INDEX; Schema: syncro; Owner: postgres
--

CREATE INDEX idx_trans_cuenta ON syncro.transacciones USING btree (numero_cuenta);


--
-- Name: idx_trans_fecha; Type: INDEX; Schema: syncro; Owner: postgres
--

CREATE INDEX idx_trans_fecha ON syncro.transacciones USING btree (fecha_hora);


--
-- Name: cuentas cuentas_dni_dueno_fkey; Type: FK CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.cuentas
    ADD CONSTRAINT cuentas_dni_dueno_fkey FOREIGN KEY (dni_dueno) REFERENCES syncro.clientes(dni) ON DELETE CASCADE;


--
-- Name: transacciones transacciones_numero_cuenta_destino_fkey; Type: FK CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.transacciones
    ADD CONSTRAINT transacciones_numero_cuenta_destino_fkey FOREIGN KEY (numero_cuenta_destino) REFERENCES syncro.cuentas(numero_cuenta);


--
-- Name: transacciones transacciones_numero_cuenta_fkey; Type: FK CONSTRAINT; Schema: syncro; Owner: postgres
--

ALTER TABLE ONLY syncro.transacciones
    ADD CONSTRAINT transacciones_numero_cuenta_fkey FOREIGN KEY (numero_cuenta) REFERENCES syncro.cuentas(numero_cuenta);


--
-- PostgreSQL database dump complete
--

\unrestrict EMDEw5ocgZQcJ3pjrd2K1Y3FvD5aiNzhAYWJIlhuTZHO2SLZ7KslqXWt6XmmvF3


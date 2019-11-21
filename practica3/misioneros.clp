;-------------------------------------------------------------
; MODULO MAIN
;-------------------------------------------------------------
(defmodule MAIN (export deftemplate nodo)
(export deffunction heuristica)
(export defglobal capacidad)
(export deffunction problema))
;;; Definición del nodo para la exploración
(deftemplate MAIN::nodo
    (multislot estado)
    (multislot camino)
    (slot heuristica)
    (slot coste (default 0))
    (slot clase (default abierto)))

;;; Definición de variables globales y valores iniciales
(defglobal MAIN
    ?*estado-inicial* = (create$ m m m s c c c i s)
    ?*estado-final* = (create$ s f m m m s c c c )
    ?*ci* = (create$ )
    ?*mi* = (create$ )
    ?*misioneros* = 3
    ?*canibales* = 3
    ?*capacidad* = 2
)

;;; Definición de la heurística
;;; numero de misioneros mas numero de caníbales mi+ci
(deffunction MAIN::heuristica (?mi ?ci)
    (bind ?res (+ (length$ ?mi) (length$ ?ci)))
    ?res)

;;; A* REGLA de CONTROL/Estrategia de búsqueda (COMPLETA)
(defrule MAIN::pasa-el-mejor-a-cerrado
   ?nodo <- (nodo (clase abierto)
                  (coste ?c1) 
                  (heuristica ?h1))
   (not (nodo (clase abierto)
              (heuristica ?h2)
              (coste ?c2&:(< (+ ?h2 ?c2) (+ ?h1 ?c1) ) ) ) )
=> 
   (modify ?nodo (clase cerrado))
   (focus OPERADORES))

;-------------------------------------------------------------
; MODULO OPERADORES
;-------------------------------------------------------------
; Acciones del Problema: 1M, 2C, 2M, 1C1M de la orilla inicial
; a la final O de la orilla final a la inicial
;-------------------------------------------------------------
(defmodule OPERADORES
    (import MAIN deftemplate nodo)
    (import MAIN deffunction heuristica)
    (import MAIN defglobal capacidad))

;;;; 1C, 1M, 2C, 2M, 1C1M de la orilla inicial a la final
(defrule OPERADORES::MC-F
    (nodo (estado $?rmi $?mi s $?rci
        $?ci&:(and (>= (+ (length$ ?mi) (length$ ?ci)) 1)
        (<= (+ (length$ ?mi) (length$ ?ci)) ?*capacidad*))
        i $?mf s $?cf)
    (camino $?movimientos)(coste ?coste)(clase cerrado))
    =>
    (bind $?nuevoestado (create$ ?rmi s ?rci f ?mf ?mi s ?cf ?ci))
    (assert (nodo (estado ?nuevoestado)
    (camino ?movimientos (implode$ ?nuevoestado))
    (coste (+ ?coste 1))
    (heuristica (heuristica ?rmi ?rci)))))

;;;; 1C, 1M, 2C, 2M, 1C1M de la orilla final a la inicial (COMPLETA)
(defrule OPERADORES::MC-I
    (nodo (estado $?mf s $?cf f $?rmi $?mi s $?rci
        $?ci&:(and (>= (+ (length$ ?mi) (length$ ?ci)) 1)
        (<= (+ (length$ ?mi) (length$ ?ci)) ?*capacidad*)))
    (camino $?movimientos)(coste ?coste)(clase cerrado))
    =>
    (bind $?nuevoestado (create$ ?mf ?mi s ?cf ?ci i ?rmi s ?rci ))
    (assert (nodo (estado ?nuevoestado)
    (camino ?movimientos (implode$ ?nuevoestado))
    (coste (+ ?coste 1))
    (heuristica (heuristica ?rmi ?rci)))))

;-------------------------------------------------------------
; MODULO RESTRICCIONES
;-------------------------------------------------------------
(defmodule RESTRICCIONES
    (import MAIN deftemplate nodo))

; eliminamos nodos repetidos (COMPLETAR)
(defrule RESTRICCIONES::repeticiones-de-nodo
   (declare (auto-focus TRUE))
   ?nodo1 <- (nodo (estado $?actual)
         (camino $?movimientos1))
   ?nodo2 <- (nodo (estado $?actual)
                  (camino $?movimientos2&:(>= (length $?movimientos2) (length $?movimientos1)))) ; El camino hasta el nodo1 es menor que hasta el nodo2
   (test (neq ?nodo1 ?nodo2))
 =>
   (retract ?nodo2))

;; Eliminamos nodos con más caníbales que misioneros en una orilla (COMPLETAR)
(defrule RESTRICCIONES::eliminar-prohibidos
    (declare (auto-focus TRUE))
    ?nodo <- (nodo (estado $?mi s $?ci ?l&:i|f $?md s $?cd ))
    (test (or (and (< (length $?mi) (length $?ci)) (neq (length $?mi) 0)) (and (< (length $?md) (length $?cd)) (neq (length $?md) 0)) ))
    =>
    (bind $?nuevoestado (create$ $?mi s $?ci ?l $?md s $?cd ))
    (printout t "El estado " (implode$ $?nuevoestado) " es peligroso" crlf)
    (retract ?nodo)
    )

;-------------------------------------------------------------
; MODULO SOLUCION
;-------------------------------------------------------------
;;; Función para definir problemas de distinto número
;;; de misioneros, caníbales, y capacidad de la barca.
;;; Por ejemplo:
;;; (problema 3 3 2)
;;; (problema 5 5 3)
(defmodule SOLUCION
    (import MAIN deftemplate nodo))
;miramos si hemos encontrado la solución
(defrule SOLUCION::encuentra-solucion
    (declare (auto-focus TRUE))
    ?nodo1 <- (nodo (estado s f $?m s $?c) (camino $?camino1))
    =>
    (retract ?nodo1)
    (assert (solucion ?camino1)))

;escribimos la solución por pantalla
(defrule SOLUCION::escribe-solucion
    (solucion $?movimientos)
    =>
    (printout t "La solucion tiene " (length$ ?movimientos) " pasos" crlf)
    (loop-for-count (?i 1 (length$ ?movimientos))
    (printout t (nth$ ?i $?movimientos) crlf))
    (halt))

;-------------------------------------------------------------
; MODULO MAIN (última mención modulo MAIN, para
; poder invocar funciones de este módulo
;-------------------------------------------------------------
;;; Para ejecutar el programa
;;; (load "misioneros.clp")
;;; (problema 3 3 2)
(deffunction MAIN::problema (?m ?c ?b)
    ;(reset)
    (bind ?*misioneros* ?m)
    (bind ?*canibales* ?c)
    (bind ?*capacidad* ?b)
    (loop-for-count (?i 1 ?m)
    (bind ?*mi* (create$ ?*mi* m)))
    (loop-for-count (?i 1 ?c)
    (bind ?*ci* (create$ ?*ci* c)))
    (bind ?*estado-inicial* (create$ ?*mi* s ?*ci* i s))
    (bind ?*estado-final* (create$ s f ?*mi* s ?*ci*))
    (assert (nodo (estado ?*estado-inicial*)
    (camino (implode$ ?*estado-inicial*))
    (heuristica (heuristica ?*ci* ?*mi*))
    ))
    ;(run)
)
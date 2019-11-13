;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;           MODULO MAIN           ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmodule MAIN 
	(export deftemplate nodo)
   (export deffunction heuristica))

(deftemplate MAIN::nodo
   (multislot estado)
   (multislot camino)
   (slot heuristica)
   (slot coste)
   (slot clase (default abierto)))

(defglobal MAIN
   ?*estado-inicial* = (create$ B B B H V V V)
   ?*estado-final* = (create$ V V V H B B B))

(deffunction MAIN::heuristica ($?estado)
   (bind ?res 0)
   (loop-for-count (?i 1 7)
    (if (neq (nth ?i $?estado)
             (nth ?i ?*estado-final*))
         then (bind ?res (+ ?res 1))
     )
    )
   ?res)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;     MODULO MAIN::INICIAL        ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defrule MAIN::inicial
   =>
   (assert (nodo 
              (estado ?*estado-inicial*)
              (camino)
              (coste 0)
              (heuristica (heuristica ?*estado-inicial*))
              (clase abierto)))
 )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; MODULO MAIN::CONTROL            ;;;
;;; PRIMERO EL MEJOR                ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; MODULO OPERACIONES              ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmodule OPERADORES
   (import MAIN deftemplate nodo)
   (import MAIN deffunction heuristica))

              
(defrule OPERADORES::izquierda
   (nodo (estado $?a ?b $?y&:(<= (length$ ?y) 2) H $?d)
          (camino $?movimientos)
          (coste ?c)
          (clase cerrado))
 =>
   (bind $?nuevo-estado (create$ $?a H  $?y ?b $?d))
   (assert (nodo
		      (estado $?nuevo-estado)
              (camino $?movimientos (implode$ ?nuevo-estado))
              (heuristica (heuristica $?nuevo-estado))
              (coste (+ ?c 1)))))


(defrule OPERADORES::derecha
   (nodo (estado $?a H $?y&:(<= (length$ ?y) 2) ?b $?d)
          (camino $?movimientos)
          (coste ?c)
          (clase cerrado))
 =>
   (bind $?nuevo-estado (create$ $?a ?b $?y  H $?d))
   (assert (nodo
		      (estado $?nuevo-estado)
              (camino $?movimientos (implode$ ?nuevo-estado))
              (heuristica (heuristica $?nuevo-estado))
              (coste (+ ?c 1)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;      MODULO RESTRICCIONES       ;;;
;;; OJO ESTA REGLA NO ESTA BIEN  ...;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmodule RESTRICCIONES
   (import MAIN deftemplate nodo))

(defrule RESTRICCIONES::repeticiones-de-nodo
   (declare (auto-focus TRUE))
   ?nodo1 <- (nodo (estado $?actual)
         (camino $?movimientos1))
   ?nodo2 <- (nodo (estado $?actual)
                  (camino $?movimientos2&:(>= (length $?movimientos2) (length $?movimientos1)))) ; El camino hasta el nodo1 es menor que hasta el nodo2
   (test (neq ?nodo1 ?nodo2))
 =>
   (retract ?nodo2))
   
  

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;    MODULO MAIN::SOLUCION        ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmodule SOLUCION
   (import MAIN deftemplate nodo))

(defrule SOLUCION::reconoce-solucion
   (declare (auto-focus TRUE))
   ?nodo <- (nodo (estado V V V H B B B)
               (camino $?movimientos))
 => 
   (retract ?nodo)
   (assert (solucion $?movimientos)))

(defrule SOLUCION::escribe-solucion
   (solucion $?movimientos)
  =>
   (printout t "La solucion tiene " (length$ ?movimientos) " pasos" crlf)
   (loop-for-count (?i 1 (length$ ?movimientos))
   (printout t (nth ?i $?movimientos) crlf))
   (halt))



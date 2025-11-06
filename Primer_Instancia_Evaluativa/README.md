#  Casino de Dados - 1° Instancia Evaluativa

##  Cómo jugar
1. El programa pide:
   - Cantidad de jugadores (2–4, sin contar al Casino).
   - Nombre real, apodo validado, tipo de jugador (Novato, Experto o VIP).
   - Dinero inicial con el que ingresa cada jugador.

2. En cada ronda:
   - Cada jugador apuesta un monto válido (mínimo 1 y máximo su saldo).
   - Todos tiran dos dados → aplicando trampas de La Casa.
   - El VIP puede usar **una vez por partida** un reroll.
   - El ganador recibe el pozo; si hay empate se reparte.

3. Entre rondas se pueden usar comandos:
   - `STATS` → estadísticas actuales
   - `HISTORY` → historial de partidas (máx 5)
   - `RANKING` → ranking de jugadores por dinero
   - `TRAMPAS` → registro de trampas de La Casa
   - `SAVE [nombre]` → guardar partida con nombre personalizado
   - `QUIT` → salir del juego
   - `CONTINUAR` → pasar a la siguiente ronda

4. El juego termina a las 3 rondas o cuando un jugador se queda sin dinero.

---
 
Algunos de los prompts que usamos fueron:

- *“Necesito crear una clase abstracta Jugador en Java con atributos privados nombre, dinero, partidasGanadas y métodos abstractos calcularApuesta y obtenerTipoJugador. ¿Cómo la armo?”*
- *“Haceme las subclases JugadorNovato, JugadorExperto y JugadorVIP que extiendan de Jugador, con un método obtenerTipoJugador() que devuelva su tipo.”*
- *“Cómo hago una clase Dado en Java que devuelva un número aleatorio del 1 al 6.”*
- *“Quiero que el Casino haga trampas con 30% de probabilidad: o le sale 6 en un dado, o le baja -1 a otro jugador. ¿Cómo puedo implementarlo?”*
- *“Necesito registrar estadísticas: mayor apuesta, mejor puntaje, total de partidas y víctimas de trampas. ¿Qué estructura de datos me conviene usar?”*
- *“Armame un procesador de comandos en Java que entienda: STATS, HISTORY, RANKING, TRAMPAS, SAVE [nombre], CONTINUAR y QUIT.”*
- *“Cómo valido en Java que un apodo tenga entre 3 y 10 caracteres y solo letras y espacios usando regex.”*
- *“Quiero armar un historial de partidas con las últimas 5 jugadas, usando StringBuilder en el formato: PARTIDA #1 - Jugadores: ... | Ganador: ... | Rondas: x.”*
- *“Cómo puedo mostrar un ranking final de jugadores ordenado por dinero en Java.”*

---

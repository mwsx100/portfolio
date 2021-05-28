
;;;; ASSIGNMENT 5
;;;;
;;;; Connect-Four Player
;;;; Due Midnight, the evening of WEDNESDAY, APRIL 28.
;;;;
;;;;
;;;; You will provide the TA a single file (a revised version of this one).
;;;; You'll write the heuristic board evaluation function and the alpha-beta searcher
;;;; for a CONNECT FOUR game.  If you don't know how to play connect four, and can't
;;;; find stuff online, get ahold of me.  The version of connect four we'll be playing
;;;; is NON-TOROIDAL (non-"wraparound").
;;;;
;;;;
;;;;
;;;; HOW TO USE THIS TEMPLATE FILE
;;;;
;;;; This is the file you will fill out and provide to the TA.  It contains your project-specific
;;;; code.  Don't bother providing the connectfour.lisp file -- we have it, thanks.
;;;;
;;;; I'm providing you with one function the MAKE-COMPUTER-MOVE function.  This function 
;;;; takes a game state and generates all one-move-away states from there.  It then calls 
;;;; ALPHA-BETA on those states returns the new game state which got the highest alpha-beta 
;;;; score.  Piece of cake.
;;;;
;;;; You will write:
;;;;
;;;; 1. The ALPHA-BETA function.  This is a straightforward implementation of the
;;;;    function described in the lecture notes.  However to make your player better
;;;;    you MIGHT (or might not! -- it could make things worse) sort the expanded children
;;;;    based on who's best, so as to increase the chance that alpha-beta will cut off
;;;;    children early and improve your search time.  Look into the SORT function.
;;;;
;;;; 2. The EVALUATE function.  Or more specifically, you'll fill out one part of
;;;;    it (the progn block shown).  Here you'll provide an intelligent heuristic
;;;;    evaluator.  Right now the evaluator provided ("0") literally says that
;;;;    all unfinished games are draws.  That's pretty stupid -- surely you can
;;;;    do better than that!  You're welcome, and perhaps encouraged, to write your
;;;;    own separate function(s) and call it from within the EVALUATE function so as
;;;;    to keep the EVALUATE function code clean.  Keep whatever functions and
;;;;    auxillary code to within this file please.
;;;;
;;;; Your code should work for any width or height board, and for any number of
;;;; checkers in a row to win (at least 2).  These values can be extracted from
;;;; game board functions in the connectfour.lisp file.  Indeed, I'd strongly suggest
;;;; examining all of that file as it may contain some valuable functions and constants
;;;; that you might want to know about.
;;;;
;;;; Your code will not only be in this file but will be in its own package as well.
;;;; Your package name will be :firstname-lastname
;;;; For example, the package name for MY package is called :sean-luke
;;;; will change the text :TEMPLATE with your :firstname-lastname package name
;;;; in the TWO places it appears in this file.
;;;;
;;;; You will then name your file "firstname-lastname.lisp".  For example, I would
;;;; name my own file "sean-luke.lisp"
;;;;
;;;;
;;;; RUNNING THE CODE
;;;;
;;;; You'll want to compile your code first, like I do to my code in this example:
;;;;
;;;; (load "connectfour")
;;;; (compile-file "connectfour.lisp")
;;;; (load "connectfour")
;;;; (load "sean-luke")
;;;; (compile-file "sean-luke.lisp")
;;;; (load "sean-luke")
;;;;
;;;; Now we're ready to go.  If you want to just do a quick human-human game, you
;;;; don't need to compile and load your own file, just the connectfour file.
;;;;
;;;; You can run a simple human-against-human example like this:
;;;;
;;;; (play-human-human-game)
;;;;
;;;; You can run a simple human-against-computer example like this (replace sean-luke
;;;; with your own package name in this example of course)
;;;;
;;;; (play-human-computer-game #'sean-luke:make-computer-move t)  
;;;;
;;;; ...or if you wish the computer to go first,
;;;;
;;;; (play-human-computer-game #'sean-luke:make-computer-move nil)
;;;;
;;;; You can play a head-to-head game against two computer players (loading both of course)
;;;; like this:
;;;;
;;;; (play-game #'sean-luke:make-computer-move #'keith-sullivan:make-computer-move)
;;;;
;;;; Note that for all three of these functions (play-human-human-game, play-human-computer-game,
;;;; play-game) there are lots of optional keywords to change the width o the board, the height,
;;;; the number in a row that must be achieved, the search depth, whether boards are printed, etc.
;;;;
;;;;
;;;;
;;;; TESTING POLICY
;;;;
;;;; Plagiarism rules still apply (no code given, no code borrowed from anyone or any source
;;;; except the TA and the Professor, do not lay eyes on other people's code anywhere, including
;;;; online).  However they are softened in the following fashion:
;;;;
;;;;    - You are strongly encouraged to play against each other as long as neither of you
;;;;      looks at each others' code nor discusses your alpha-beta code.  Go on the forum and
;;;;      ask for partners.
;;;;
;;;;    - You are NOT permitted to discuss, in any way, the ALPHA-BETA function with anyone
;;;;      except ME and the TA.  That has to be done entirely on your own. 
;;;;
;;;;    - You ARE allowed to discuss, in general and HIGHLY ABSTRACT terms, your own approach
;;;;      to the EVALUATE function.  You are not allowed to show code or read another student's
;;;;      code.  But you can talk about your theory and general approach to doing board evaluation
;;;;      for connect four.  Let a thousand heuristics bloom!  You are also welcome to
;;;;      go online and get ideas about how to do the EVALUATE function as long as you do NOT
;;;;      read code.  I'm just as good as the next person -- perhaps even better -- at gathering
;;;;      every scrap of connect four code on the web and comparing your code against it.
;;;;
;;;; OTHER POLICIES
;;;;
;;;; - This code may have, and almost certainly has, bugs.  I'll post revisions as needed.
;;;;
;;;; - Near the end of the exam we will have a single-elimination tournament competition.
;;;;      The winner of the competition gets to play against the professor's "better" evaluator
;;;;      (assuming I've written one by then) and ALSO receives an improvement in his overall
;;;;      course grade.  Runners up may receive something smaller: at least a candy bar, perhaps
;;;;      a better incentive.
;;;;
;;;; - If your evaluator is too slow, you will be penalized with a cut in your search depth.
;;;;   Thus it's in your best interest to have as good an evaluator/depth combination as possible.
;;;;   Note that it's often the case that evaluators that are good on ODD depths are bad on EVEN
;;;;   depths (or vice versa!).  I won't say what depth will be used in the tournament.
;;;;
;;;; - To make your code run as fast as possible, I strongly suggest you look into Lisp compiler optimization.  SBCL's profiler might prove helpful too.
;;;;
;;;; Good luck!
;;;;
;;;;

(defpackage :matthew-souvannaphandhu 
  (:use "COMMON-LISP" "COMMON-LISP-USER")
  (:export "MAKE-COMPUTER-MOVE"))

(in-package :matthew-souvannaphandhu)

 (defun alpha-beta (game current-depth max-depth
		   is-maxs-turn-p expand terminal-p evaluate
		   alpha beta) 
	   (if (or (funcall terminal-p game) (> current-depth max-depth))
	       (return-from alpha-beta (funcall evaluate game is-maxs-turn-p )))
	   (if (funcall is-maxs-turn-p game)
	       (let ( (sc alpha))
		 (dolist (x (funcall expand game))
		   (let ( (mm (alpha-beta x (+ 1 current-depth) max-depth is-maxs-turn-p
					  expand terminal-p #'evaluate 1000 -1000)))
		     (setf sc (max mm sc))))
		 (return-from alpha-beta sc))
	       (let ( (sc beta))
		 (dolist (x (funcall expand game))
		   (let ( (mm (alpha-beta x (+ 1 current-depth) max-depth is-maxs-turn-p
					  expand terminal-p #'evaluate 1000 -1000)))
		     (setf sc (min mm sc))))
		 (return-from alpha-beta sc))))
					  
 "Does alpha-beta search. 
is-maxs-turn-p takes one argument (a game) and returns true if it's max's turn in that game.  
expand takes one argument (a game) and gives all the immediate child games for this game.
terminal-p takes one argument (a game) and returns true if the game is over or not.
evaluate takes TWO arguements (a game and the is-maxs-turn-p function) and provides a quality
assessment of the game for 'max', from min-wins (-1000) to max-wins (1000)."

;;; IMPLEMENT ME


(defun x-left (game col row) ;these functions check if the piece is blocked in various directions
  (let ((i (- (num-in-a-row game) 1)))
	   (if (< col i) (return-from x-left nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (l-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b (- col (+ x 1)) row)))
		 (if (and (/= p piece) (/= p 0)) (return-from x-left nil)
		    (progn (setf l-list (cons (list p (- col (+ x 1)) row) l-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf l-list (cons t l-list))
		 (setf l-list (cons nil l-list)))
	     l-list)))
		     
		 
 (defun x-right (game col row) 
   (let ((i (- (num-in-a-row game) 1)))
	   (if (> col i) (return-from x-right nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (r-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b (+ col (+ x 1)) row)))
		 (if (and (/= p piece) (/= p 0)) (return-from x-right nil)
		    (progn (setf r-list (cons (list p (+ col (+ x 1)) row) r-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf r-list (cons t r-list))
		 (setf r-list (cons nil r-list)))
	     r-list)))

 (defun x-up (game col row) 
    (let ((i (- (num-in-a-row game) 1)))
	   (if (> row (- i 1)) (return-from x-up nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (u-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b  col (+ (+ x 1) row))))
		 (if (and (/= p piece) (/= p 0)) (return-from x-up nil)
		    (progn (setf u-list (cons (list p col (+ (+ x 1) row)) u-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf u-list (cons t u-list))
		 (setf u-list (cons nil u-list)))
	     u-list)))

 (defun x-up-left (game col row) 
    (let ((i (- (num-in-a-row game) 1)))
	   (if (or (> row (- i 1)) (< col i) )(return-from x-up-left nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (ul-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b (- col (+ x 1)) (+ (+ x 1) row))))
		 (if (and (/= p piece) (/= p 0)) (return-from x-up-left nil)
		    (progn (setf ul-list (cons (list p (- col (+ x 1)) (+ (+ x 1) row)) ul-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf ul-list (cons t ul-list))
		 (setf ul-list (cons nil ul-list)))
	     ul-list)))

(defun x-up-right (game col row)
   (let ((i (- (num-in-a-row game) 1)))
	   (if (or (> row (- i 1)) (> col i) )(return-from x-up-right nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (ur-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b (+ col (+ x 1)) (+ (+ x 1) row))))
		 (if (and (/= p piece) (/= p 0)) (return-from x-up-right nil)
		    (progn (setf ur-list (cons (list p (+ col (+ x 1)) (+ (+ x 1) row)) ur-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf ur-list (cons t ur-list))
		 (setf ur-list (cons nil ur-list)))
	     ur-list)))

(defun x-d-left (game col row) 
   (let ((i (- (num-in-a-row game) 1)))
	   (if (or (< row i) (< col i) )(return-from x-d-left nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (dl-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b (- col (+ x 1)) (- row (+ x 1)))))
		 (if (and (/= p piece) (/= p 0)) (return-from x-d-left nil)
		    (progn (setf dl-list (cons (list p (- col (+ x 1)) (- row (+ x 1))) dl-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf dl-list (cons t dl-list))
		 (setf dl-list (cons nil dl-list)))
	     dl-list)))

(defun x-d-right (game col row)
   (let ((i (- (num-in-a-row game) 1)))
	   (if (or (< row i) (> col i) )(return-from x-d-right nil))
	   (let* ( (b (board game)) (piece (aref b col row)) 
		   (dr-list (list (list piece col row))) (w 0))
	     (dotimes (x i)
	       (let ((p (aref b (+ col (+ x 1)) (- row (+ x 1)))))
		 (if (and (/= p piece) (/= p 0)) (return-from x-d-right nil)
		    (progn (setf dr-list (cons (list p (+ col (+ x 1)) (- row (+ x 1))) dr-list))
			   (if (= p piece) (incf w))))))
	     (if (>= w (- i 1)) (setf dr-list (cons t dr-list))
		 (setf dr-list (cons nil dr-list)))
	     dr-list)))






(defun critical (c-list) 
	   (let ((cr (cdr c-list))) 
	     (dolist (x cr)
	       (if (= 0 (car x)) (return-from critical (list (cadr x) (caddr x)))))))

(defun crit-fit (crit game color)
	   (let ( (tl (remaining-moves game)) (space (- 6 (row-for-move (car crit) game)))
		  (row (cadr crit)))
	     (if (= 0 (rem (- tl space) 2)) 
		 (if (= (turn game) color)
		     (if (= 1 (rem row 2)) (return-from crit-fit t)
			 (return-from crit-fit nil)) 
		     (if (= 0 (rem row 2)) (return-from crit-fit t) 
			 (return-from crit-fit nil)))
		 (if (= (turn game) color)
		     (if (= 0 (rem row 2)) (return-from crit-fit t) 	     
					(return-from crit-fit nil))
		     (if (= 1 (rem row 2)) (return-from crit-fit t) 
			 (return-from crit-fit nil))))))
;crit-fit evaluates a 3 in a row and sees how good it is
;it focuses on the column the critical move would be played in and 
;assumes a future state in the game where all other spaces except for that
;column has been played. First it computes the remaining moves minus the
;empty spaces in the column, if the difference is even and it is
;your turn, you would be the first to play in that column
;if you are the first to play in that column, you want the row of your crit move 
;to be odd, because your opponent can only play on even rows


(defun liberties (game col row color) ;finds how many directions a piece can connect 4 from
	   (let* ( (l (x-left game col row)) (r (x-right game col row)) ;starting at that piece 
		   (u (x-up game col row)) (ul (x-up-left game col row))
		   (ur (x-up-right game col row)) (dl (x-d-left game col row))
		   (dr (x-d-right game col row)) (c 0) (c-list (list)) (w-list (list)))	     
	     (dolist (x (list l r u ul ur dl dr))
	        (if (not (equalp nil x)) 
		    (progn (setf c-list (cons x c-list)) (incf c)
			   (if (equalp (car x) t)
			       (progn  (let ( (crit (critical x))) ;also finds 3 in a rows while its at it
					 (setf w-list (cons crit w-list)) 
					 (if (equalp t (crit-fit crit game color)) ;tries to evaluate critical moves
					     (incf c) 
					     (setf c (+ c .5)))))))))
	     (if (not (equalp nil w-list)) (setf w-list (remove-duplicates w-list)))
	     (list c w-list c-list)))

(defun same-crit-col (b-crit r-crit) ;basically finds where red and black have critical moves
	   (let ((c-list (list)) (m-list (list))  ) ;in the same column
	   (dolist (x b-crit)
	     (if (not (member (car x) c-list))
		 (setf c-list (cons (car x) c-list))))
	   (dolist (y r-crit)
	     (if (member (car y) c-list)
		 (if (not (member (car y) m-list))
		   (setf m-list (cons (car y) m-list)))))
	   (if (equalp nil m-list) (return-from same-crit-col nil)
	       (let ((mc-list (list)))
		 (dolist (x m-list)
		   (let ((b-list (list)) (r-list (list)) )
		     (dolist (y b-crit)
		       (if (= x (car y)) (setf b-list (cons y b-list))))
		     (dolist (z r-crit)
		       (if (= x (car z)) (setf r-list (cons z r-list))))
		     (setf mc-list (cons (list x b-list r-list) mc-list))))
		 mc-list)))) ;returns a list of lists for each column that has critical moves from both

(defun comp-crit (game color min 2nd-min col) ;compares 2 critical moves and how good they are
	     (if (and (= (cadr min) color) (= (cadr 2nd-min) color)) ;;both mins are your color, best 
		 (return-from comp-crit 4)) ;;return values I chose haphazardly, i had trouble coming up with good values
	     (if (and (= (cadr min) color) (= (cadr 2nd-min) (- color ))) ;first min is you, 2nd is opponent 
		 (if (equalp t (crit-fit (list col (car min)) game color)) (return-from comp-crit 1.5)
		     (if (equalp t (crit-fit (list col (car 2nd-min)) game (- color))) (return-from comp-crit 0.5))))
	     (if (and (= (cadr min) color) (= (cadr 2nd-min) 11)) ;1st min you, 2nd is both
		 (if (equalp t (crit-fit (list col (car min)) game color)) (return-from comp-crit 1.7)
		     (if (equalp t (crit-fit (list col (car 2nd-min)) game color)) (return-from comp-crit 1))))
	     (if (= (cadr min) 11) (if (equalp t (crit-fit (list col (car min)) game  color)) ;first min is tie
				       (return-from comp-crit 1))
		 (return-from comp-crit 0)) 0)

(defun group-crit (x-list game color) ;;finds the 2 minimum rows of crits in the same column
	   (let* ((col (car x-list)) (c-list (cdr x-list)) 
		  (b (car c-list)) (r (cadr c-list)) (2nd-min (list 8 0)) (min (list 8 0))) 
	     (dolist (x b)
	       (if (< (cadr x) (car min))
			  (setf min (list (cadr x) 1))
			  (if (< (cadr x) (car 2nd-min)) (setf 2nd-min (list (cadr x) 1)))))
	     (dolist (y r)
	       (if (< (cadr y) (car min))
		   (setf min (list (cadr y) -1))
		   (if (= (cadr y) (car min)) (setf (cadr min) 11)
		       (if (< (cadr y) (car 2nd-min)) (setf 2nd-min (list (cadr y) -1))
			     (if (= (cadr y) (car 2nd-min)) (setf (cadr 2nd-min) 11))))))
	     (comp-crit game color min 2nd-min col))) ;goes into comp-crit and returns from it


	     

(defun in-play (game) ;scans the board and returns a list of every piece in play
	   (let ( (b (board game)) (b-list (list)) (r-list (list)) )
	     (dotimes (x (width game))
	       (block column
		 (dotimes (y (height game))
		   (let ((p (aref b x y)))
		   (if (= 0 p) (return-from column)
		      (progn (if (= 1 p) (setf b-list (cons (list p x y) b-list))
				 (setf r-list (cons (list p x y) r-list)))))))))
	     (list b-list r-list)))

(defun score (game color) ;puts all previous functions together
	   (let* ((g-list (in-play game)) (b-list (car g-list)) (r-list (cadr g-list)) (b-sc 0) (r-sc 0)
		  (b-crit (list)) (r-crit (list)))
	     (dolist (x b-list) ;gets the liberties of each piece
	       (let* ((col (cadr x)) (row (caddr x)) (bl-lib (liberties game col row 1)))
	       (setf b-sc (+ b-sc (car bl-lib)))
	       (if (not (equalp nil (cadr bl-lib))) (setf b-crit (append b-crit (cadr bl-lib))))))
	     (dolist (y r-list)
	       (let* ((col (cadr y)) (row (caddr y)) (r-lib (liberties game col row -1)))
	       (setf r-sc (+ r-sc (car r-lib)))
	       (if (not (equalp nil (cadr r-lib))) (setf r-crit (append r-crit (cadr r-lib))))))
	     (setf b-crit (remove-duplicates b-crit :test #'equalp)) 
	     (setf r-crit (remove-duplicates r-crit :test #'equalp))
	     (block critical ;evaluates potential winning moves
	     (let ((scc (same-crit-col b-crit r-crit)))
	       (if (equalp nil scc) (return-from critical)
		   (dolist (x scc) 
		    (setf b-sc (+ b-sc (group-crit x game 1)))
		    (setf r-sc (+ r-sc (group-crit x game -1)))))))

	     (if (= 1 color) (return-from score (- b-sc r-sc))
		 (return-from score (- r-sc b-sc)))))

(defun evaluate (game is-maxs-turn-p)
  "Returns an evaluation, between min-wins and max-wins inclusive, for the game.
is-maxs-turn-p is a function which, when called and passed the game, returns true
if it's max's turn to play."
  (let ((end (game-over game)))
    (if (null end) ;; game not over yet

	(progn

	  ;;; IMPLEMENT ME
	  ;; in this block, do your code which returns a heuristic
	  ;; evaluation of the system.  Feel free to create an outside function
	  ;; and call it if you don't want all the code here.
;	  (score game (turn game))
	  (if (funcall is-maxs-turn-p game) (score game (turn game))
	      (score game (- (turn game))))
	     ;; by default we're returning 0 (draw).  That's obviously wrong.

	  ;;; END IMPLEMENTATION

	  )


	(if (= 0 end)  ;; game is a draw
	    0
	    ;; else, the game is over but not a draw.  Return its value.
	    ;;
	    ;; this is a deep-math way of saying the following logic:
	    ;;
	    ;; if black, then if turn=black and ismaxsturn, then max-wins
	    ;; if black, then if turn=red and ismaxsturn, then min-wins
	    ;; if black, then if turn=black and !ismaxsturn, then min-wins
	    ;; if black, then if turn=red and !ismaxsturn, then max-wins
	    ;; if red, then if turn=black and ismaxsturn, then min-wins
	    ;; if red, then if turn=red and ismaxsturn, then max-wins
	    ;; if red, then if turn=black and !ismaxsturn, then max-wins
	    ;; if red, then if turn=red and !ismaxsturn, then min-wins
	    ;;
	    ;; keep in mind that black=1, red=-1, max-wins=1000, red-wins = -1000

	    (* end (turn game) max-wins (if (funcall is-maxs-turn-p game) 1 -1))))))



;; I've decided to make this function available to you
;(defun expand (gm) (all-moves gm))
;(defun is-maxs-turn-p (gm) (= (turn gm) max))
;(defun terminal-p (gm) (game-over gm))

(defun make-computer-move (game depth)
  "Makes a move automatically by trying alpha-beta on all possible moves and then picking
the one which had the highest value for max., and making that move and returning the new game."

  (let* ((max (turn game)))
    (print (score (max-element (all-moves game)
		 (lambda (g)
		   (alpha-beta g 0 depth 
			       (lambda (gm) (= (turn gm) max)) ;; is-maxs-turn-p
			       (lambda (gm) (all-moves gm)) ;; expand
			       (lambda (gm) (game-over gm)) ;; terminal-p
			       #'evaluate ;; evaluate
			       min-wins
			       max-wins))) max))))

;; go back to cl-user
(in-package :cl-user)

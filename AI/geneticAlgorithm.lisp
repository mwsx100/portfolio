
;;; Some utility Functions and Macros that you might find to be useful (hint)

(defmacro while (test &rest body)
"Repeatedly executes body as long as test returns true. Then returns nil."
`(loop while ,test do (progn ,@body)))

;;; Example usage
;;;
;;; (let ((x 0))
;;; (while (< x 5)
;;; (print x)
;;; (incf x)))


(defun random? (&optional (prob 0.5))
"Tosses a coin of prob probability of coming up heads,
then returns t if it's heads, else nil."
(< (random 1.0) prob))

(defun generate-list (num function &optional no-duplicates)
"Generates a list of size NUM, with each element created by
(funcall FUNCTION). If no-duplicates is t, then no duplicates
are permitted (FUNCTION is repeatedly called until a unique
new slot is created). EQUALP is the default test used for duplicates."
(let (bag)
(while (< (length bag) num)
(let ((candidate (funcall function)))
(unless (and no-duplicates
(member candidate bag :test #'equalp))
(push candidate bag))))
bag))

;; hope this works right
(defun gaussian-random (mean variance)
"Generates a random number under a gaussian distribution with the
given mean and variance (using the Box-Muller-Marsaglia method)"
(let (x y (w 0))
(while (not (and (< 0 w) (< w 1)))
(setf x (- (random 2.0) 1.0))
(setf y (- (random 2.0) 1.0))
(setf w (+ (* x x) (* y y))))
(+ mean (* x (sqrt variance) (sqrt (* -2 (/ (log w) w)))))))






;;;;;; TOP-LEVEL EVOLUTIONARY COMPUTATION FUNCTIONS


;;; TOURNAMENT SELECTION

;; is this a good setting? Try tweaking it (any integer >= 2) and see
(defparameter *tournament-size* 7)
(defun tournament-select-one (population fitnesses)
    (let* ((p population) ;sets p to population, best index as randomly chosen element
	   (b-ind (random (length p))) (best (nth b-ind p)) (i 2) (tor *tournament-size*))
         (while (<= i tor) ;loops until i is greater than tor
                (let* ( (n-ind (random(length p))) (next (nth n-ind p))) ;n-ind is the index of next individual randomly chosen
                     (if (> (nth n-ind fitnesses) (nth b-ind fitnesses)) ;if next is better than best, best is set to next
                          (setf best next b-ind n-ind)   )) (incf i))
          best )) ;return best
         
    
"Does one tournament selection and returns the selected individual."
     

;;; IMPLEMENT ME
;;;
;;; See Algorithm 32 of Essentials of Metaheuristics

;;; Hints:
;;; 1. My implementation is about 7 lines long
;;; 2. This would be a reasonable place to judiciously use SETF
;;; 3. You might want to do a loop, and keep track of both the
;;; best individual you've found so far and also its fitness



(defun tournament-selector (num population fitnesses)
    (generate-list num #'(lambda () (tournament-select-one population fitnesses)))) 
"Does NUM tournament selections, and puts them all in a list, then returns the list"

;;; IMPLEMENT ME
;;;
;;; Hints:
;;; 1. This is a very short function. My version is 1 line long.
;;; 2. Maybe one of the utility functions I provided might be of
;;; benefit here





;; I'm nice and am providing this for you. :-)
(defun simple-printer (pop fitnesses num)
"Determines the individual in pop with the best (highest) fitness, then
prints that fitness and individual in a pleasing manner."
(let (best-ind best-fit)
(mapcar #'(lambda (ind fit)
(when (or (not best-ind)
(< best-fit fit))
(setq best-ind ind)
(setq best-fit fit))) pop fitnesses)
(format t "~%Best Individual of Generation ~a...~%Fitness: ~a~%Individual:~a~%"
num best-fit best-ind)
fitnesses))


(defparameter *no-elites* 20)

(defun evolve (generations pop-size &key setup creator selector modifier evaluator printer)
  (let* ((p (generate-list pop-size creator)) (best nil) ) ;generates a list of pop-size random elements within the boundaries, best initially set to nil
    (dotimes (n generations) ;repeat for the amount of generations specified
      (dolist (i p) ;iterate through p
	(let ( (f-pi (funcall evaluator i) )) ;fitness of Pi
	  (if (or (equalp best nil) (> f-pi (funcall evaluator best))) (setf best (copy-seq i))))) ;if best is nil or f-pi is better, best is set to Pi
      (let ( (q (list))) ;generates empty list for Q children
      (dotimes (i (/ pop-size 2))  ;repeat for pop-size/2
	(setf q (append q (funcall modifier (car (funcall selector 1 p (mapcar evaluator p)))  (car (funcall selector 1 p (mapcar evaluator p))))))  ) ;does uniform crossover followed by mutation and adds result to q
      (setf p q) (funcall printer p (mapcar evaluator p) n))  ) ;prints the most fit individual in p
    best)) ;returns best
	  
      
    
"Evolves for some number of GENERATIONS, creating a population of size
POP-SIZE, using various functions"

;;; IMPLEMENT ME
;;;
;; The functions passed in are as follows:
;;(SETUP) called at the beginning of evolution, to set up
;; global variables as necessary
;;(CREATOR) creates a random individual
;;(SELECTOR num pop fitneses) given a population and a list of corresponding fitnesses,
;; selects and returns NUM individuals as a list.
;; An individual may appear more than once in the list.
;;(MODIFIER ind1 ind2) modifies individuals ind1 and ind2 by crossing them
;; over and mutating them. Returns the two children
;; as a list: (child1 child2). Nondestructive to
;; ind1 and ind2.
;;(PRINTER pop fitnesses) prints the best individual in the population, plus
;; its fitness, and any other interesting statistics
;; you think interesting for that generation.
;;(EVALUATOR individual) evaluates an individual, and returns its fitness.
;;Pop will be guaranteed to be a multiple of 2 in size.
;;
;; HIGHER FITNESSES ARE BETTER

;; your function should call PRINTER each generation, and also print out or the
;; best individual discovered over the whole run at the end, plus its fitness
;; and any other statistics you think might be nifty.

;;; HINTS:
;;; 1. You could do this in many ways. But I implemented it using
;;; the following functions (among others)
;;; FUNCALL FORMAT MAPCAR LAMBDA APPLY
;;; 2. My version of this function is about 20 lines long.
;;; This sounds long but it's really pretty straightforward
;;; code with no particular tricks.
;;; 3. Pay attention to the keyword arguments


(defun min-q(q)
  (let ((minq 5000))
  (dolist (n q)
    (if (< n minq) (setf minq n)))
  (position minq q)))
    

(defun n-most-fit (ind q &key evaluator)
  (if (< (length q) *no-elites*) (cons ind q)
      (progn (let ( (minq (min-q (mapcar evaluator q))) )
	(if (> (funcall evaluator ind) (funcall evaluator (elt q minq)))
	    (progn (remove (elt q minq) q) (cons ind q)) q ) ))))
		 
      
      



(defun evolve2 (generations pop-size &key setup creator selector modifier evaluator printer) ;not used, I tried to do a GA with fitness but ran out of time 
  (let* ((p (generate-list pop-size creator)) (best nil) )
    (dotimes (n generations)
      (let ( (q (list)))
      (dolist (i p) ;(print "hi")
	(let ( (f-pi (funcall evaluator i) )) ;(print "fitness pi") (print f-pi)(print "best") (print (funcall evaluator best))
	  (if (or (equalp best nil) (> f-pi (funcall evaluator best))) (setf best (copy-seq i))))
	(setf q (n-most-fit i q :evaluator evaluator))) (print (length q))
      (dotimes (i (/ (- pop-size *no-elites*) 2))  
	(setf q (append q (funcall modifier (car (funcall selector 1 p (mapcar evaluator p)))  (car (funcall selector 1 p (mapcar evaluator p))))))  )
      (setf p q) (funcall printer p (mapcar evaluator p) n))  )
    best))
	  
      







;;;;;; FLOATING-POINT VECTOR GENETIC ALGORTITHM


;;; Here you will implement creator, modifier, and setup functions for
;;; individuals in the form of lists of floating-point values.
;;; I have provided some objective functions which you can use as
;;; fitness evaluation functions.

;;; If you were really into this, you might try implementing an evolution
;;; strategy instead of a genetic algorithm and compare the two.
;;;
;;; If you were really REALLY into this, I have an extension of this
;;; project which also does genetic programming as well. That is a much
;;; MUCH MUCH MUCH more difficult project.



(defparameter *float-vector-length* 20
"The length of the vector individuals")
(defparameter *float-min* -5.12
"The minimum legal value of a number in a vector")
(defparameter *float-max* 5.12
"The maximum legal value of a number in a vector")

(defun rng () ;helper function for float-vector-creator, gives a random value with a chance of being negative
    (if (equalp t (random?)) (random 5.12)
        (* -1 (random 5.12))) )

(defun float-vector-creator ()
    (generate-list *float-vector-length* #'rng)) 
"Creates a floating-point-vector *float-vector-length* in size, filled with
UNIFORM random numbers in the range appropriate to the given problem"

;;; IMPLEMENT ME
;;;
;;; The numbers must be uniformly randomly chosen between *float-min* and
;;; *float-max*. See the documentation for the RANDOM function.

;;; HINTS:
;;; 1. My version is 3 lines long.
;;; 2. Maybe a function I provided in the utilities might be handy.


;; I just made up these numbers, you'll probably need to tweak them
(defparameter *crossover-probability* 0.05
"Per-gene probability of crossover in uniform crossover")
(defparameter *mutation-probability* 0.05
"Per-gene probability of mutation in gaussian convolution")
(defparameter *mutation-variance* 0.008
"Per-gene mutation variance in gaussian convolution")


;; to impement FLOAT-VECTOR-MODIFIER, the following two functions are
;; strongly reccommended.


(defun uniform-crossover (ind1 ind2) 
    (dotimes (n (length ind1)) (if (>= *crossover-probability* (random 1.0)) 
				   (rotatef (elt ind1 n) (elt ind2 n))) ))

"Performs uniform crossover on the two individuals, modifying them in place.
*crossover-probability* is the probability that any given allele will crossover.
The individuals are guaranteed to be the same length. Returns NIL."

;;; IMPLEMENT ME
;;;
;;; For crossover: use uniform crossover (Algorithm 25) in
;;; Essentials of Metaheuristics

;;; HINTS:
;;; 1. DOTIMES, ELT, and ROTATEF
;;; 2. My version of this function is 2 lines long.


(defun gaussian-convolution (ind)
  (let ((n  (gaussian-random 0 *mutation-variance*)))
  (dotimes (i (length ind)) ;(print (elt ind i))
    (if (>= *mutation-probability* (random 1.0))
	(progn  (loop until (and (>= (+ (elt ind i) n) *float-min*) (<= (+ (elt ind i) n) *float-max*))
		    do (setf n (gaussian-random 0 *mutation-variance*)))
	    (setf (elt ind i) (+ (elt ind i) n))) ) ) ind ))
 
  
"Performs gaussian convolution mutation on the individual, modifying it in place.
Returns NIL."

;;; IMPLEMENT ME
;;;
;;; For mutation, see gaussian convolution (Algorithm 11) in
;;; Essentials of Metaheuristics
;;; Keep in mind the legal minimum and maximum values for numbers.

;;; HINTS:
;;; 1. My version of this function is 6 lines long.
;;; 2. Maybe a function or three in the utility functions above
;;; might be handy
;;; 3. See also SETF


(defun float-vector-modifier (ind1 ind2)
  (let ( (i1 (copy-seq ind1)) (i2 (copy-seq ind2)))
    (uniform-crossover i1 i2)
    (list (gaussian-convolution i1) (gaussian-convolution i2))))
    
    
"Copies and modifies ind1 and ind2 by crossing them over with a uniform crossover,
then mutates the children. *crossover-probability* is the probability that any
given allele will crossover. *mutation-probability* is the probability that any
given allele in a child will mutate. Mutation does gaussian convolution on the allele."

;;; IMPLEMENT ME
;;; It's pretty straightforward.
;;; This function should first COPY the two individuals, then
;;; CROSS THEM OVER, then mutate the result using gaussian covolution,
;;; then return BOTH children together as a list (child1 child2)
;;;
;;; HINTS:
;;; 1. My version of this function is 6 lines long but it's
;;; really, really simple.
;;; 2. For copying lists: See the Lisp Cheat Sheet
;;; (http://cs.gmu.edu/~sean/lisp/LispCheatSheet.txt)


;; you probably don't need to implement anything at all here
(defun float-vector-sum-setup ()
"Does nothing. Perhaps you might use this function to set
(ahem) various global variables which define the problem being evaluated
and the floating-point ranges involved, etc. I dunno."
)

;;; FITNESS EVALUATION FUNCTIONS

;;; I'm providing you with some classic objective functions. See section 11.2.2 of
;;; Essentials of Metaheuristics for details on these functions.
;;;
;;; Many of these functions (sphere, rosenbrock, rastrigin, schwefel) are
;;; traditionally minimized rather than maximized. We're assuming that higher
;;; values are "fitter" in this class, so I have taken the liberty of converting
;;; all the minimization functions into maximization functions by negating their
;;; outputs. This means that you'll see a lot of negative values and that's fine;
;;; just remember that higher is always better.
;;;
;;; These functions also traditionally operate with different bounds on the
;;; minimum and maximum values of the numbers in the individuals' vectors.
;;; Let's assume that for all of these functions, these values can legally
;;; range from -5.12 to 5.12 inclusive. One function (schwefel) normally goes from
;;; about -511 to +512, so if you look at the code you can see I'm multiplying
;;; the values by 100 to properly scale it so it now uses -5.12 to 5.12.


(defun sum-f (ind)
;"Performs the Sum objective function. Assumes that ind is a list of floats"
(reduce #'+ ind))

(defun step-f (ind)
"Performs the Step objective function. Assumes that ind is a list of floats"
(+ (* 6 (length ind))
(reduce #'+ (mapcar #'floor ind))))

(defun sphere-f (ind)
"Performs the Sphere objective function. Assumes that ind is a list of floats"
(- (reduce #'+ (mapcar (lambda (x) (* x x)) ind))))

(defun rosenbrock-f (ind)
"Performs the Rosenbrock objective function. Assumes that ind is a list of floats"
(- (reduce #'+ (mapcar (lambda (x x1)
(+ (* (- 1 x) (- 1 x))
(* 100 (- x1 (* x x)) (- x1 (* x x)))))
ind (rest ind)))))

(defun rastrigin-f (ind)
"Performs the Rastrigin objective function. Assumes that ind is a list of floats"
(- (+ (* 10 (length ind))
(reduce #'+ (mapcar (lambda (x) (- (* x x) (* 10 (cos (* 2 pi x)))))
ind)))))

(defun schwefel-f (ind)
"Performs the Schwefel objective function. Assumes that ind is a list of floats"
(- (reduce #'+ (mapcar (lambda (x) (* (- x) (sin (sqrt (abs x)))))
(mapcar (lambda (x) (* x 100)) ind)))))




;;; an example way to fire up the GA. If you've got it tuned right, it should quickly
;;; find individuals which are all very close to +5.12

#|
(evolve 50 1000
:setup #'float-vector-sum-setup
:creator #'float-vector-creator
:selector #'tournament-selector
:modifier #'float-vector-modifier
:evaluator #'sum-f
:printer #'simple-printer)
|#

;I am beginning to enjoy coding in CLISP now that I am getting the hang of it. Coding in it feels more fluid than what I have experienced in object oriented programming.
; I learned another application for lambda functions when I was trying to figure out my tournament-selector function. I needed a way to set the tournament-select-one function as
; a parameter to generate-list function, and I was able to do this using lambda. My GA seems to run well when p of crossing and mutation is set to 1/vector length. I also found it
; good to set the variance to .008 from my testing on populations of size 1000. I was able to get the sum-f and step-f functions to converge on a solution but I was not able to get
; my algorithm to perform that well on the other ones. I tried to implement elitism but I have run out of time to continue working on it. The function in progress is just named evolve 
;2 and it uses  the global variable *no-fittest* to determine the n most fit individuals. I was able to get evolve2 to compile but I have this problem where my Q list keeps getting overflooded.
; I tried to use this function called n-most-fit to add elements to q but keep it under length 40. I think I should just make a separate list and then append that to Q  because later in the code,
; p gets mutated and added to Q and I think that is where it messes it up. When I was implementing evolve, at first the values were not converging at all. After a good amount of debugging,
; I found out it was not converging because I was not making copies of the individuals when I crossed them over. Basically, the list kept getting reset to random values and there was no
; progress. Once I used copy-seq, the algorithm was able to converge a lot more effectively. It was not that difficult to code the algorithm, I feel like the hard part is fully understanding
; and mastering the use of this metaheuristic. I had to do a lot of reading to figure out what I should set my global variables to, and the genetic algorithm by itself is not that powerful,
; it needs to have functions that help it optimize better. This is what I was trying to do with elitism, I plan on coming back to this and making it work. I also never got the chance to
; implement the 1/5 rule. I did not know how much O2 should be adjusted in each situation, so I still need to read up on that. The genetic algorithm is intriguing, but I would like to
; know more practical applications to it. I have gotten better at reading algorithms so I can mindlessly code up a solution, but I do not feel like I have fully grasped the scope of
; what my program is capable of. If I had more time, I would implement what I talked about above and also maybe implement a function that can tweak the global variables depending
; on the function being passed in.

package uk.ac.nottingham.ningboport.planner.Algorithms;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import uk.ac.nottingham.ningboport.planner.Network;
import uk.ac.nottingham.ningboport.planner.Route;
import uk.ac.nottingham.ningboport.planner.Solution;
import uk.ac.nottingham.ningboport.planner.Task;

public class VNS extends NFunc{
	private Network nw;
	private Solution bestSolution;
	private  int tabuLength = 7;
	private boolean tabuEnabled = false;
	private Queue<Task> tabuList = new ArrayBlockingQueue<Task>(tabuLength);
	
	private Vector<Route> currentShift;
	private int currentShiftIndex;
	
	private int shakeI;
	private int shakeBonusThreshold = 10;
	private int convergenceThreshold = 20;
	
	public VNS(Network nw) {
		super(nw);
		this.nw = nw;
		//this.best = null;
	}
	
	private boolean isTabu(Task o) {
		if (!tabuEnabled)
			return false;
		
		Iterator<Task> it = tabuList.iterator();
		while (it.hasNext()) {
			if (it.next().cmdt.id.equals(o.cmdt.id))
				return true;
		}
		
		return false;
//		if (tabuList.contains(o))
//			return true;
//		else
//			return false;
	}
	
	private void addToTabu(Task o) {
		if (tabuLength == 0 || !tabuEnabled)
			return;
		
		if (isTabu(o))
			return;
		
		if (tabuList.size() == tabuLength)
			tabuList.remove();
		
		tabuList.offer(o);
	}
	
	public void run(int seconds) {
		System.out.println("=== TS Started ===");
		Calendar eTime = Calendar.getInstance(); 
				//(Calendar) nw.alg_stime.clone();
		for (int i = 0; i < nw.routesOfShifts.size(); i++) {
			currentShiftIndex = i;
			currentShift = nw.routesOfShifts.elementAt(i);
			eTime.add(Calendar.SECOND, seconds);
			
			//Task set
			Vector<Task> mandatory = nw.getPeriodTaskSet(currentShiftIndex, true);
			Vector<Task> mandatory2 = new Vector<Task>();
			if (i != nw.routesOfShifts.size() - 1) {
				mandatory2 = nw.getPeriodTaskSet(currentShiftIndex + 1, true);
			}
			
			Vector<Task> optional = nw.getPeriodTaskSet(currentShiftIndex, false);
			
			//optimization
			int k = 0, shakeBonus = 0;
			while (Calendar.getInstance().before(eTime)) {
				switch(k) {
				case -1:
					randomSegmentOpt(9);
					break;
				
				case 0:
					if (Double.compare(move(true), 0) <= 0) {
						k++;
					} else
						k = 0;
					
					break;
					
				case 1:
					if (Double.compare(swap(true), 0) <= 0) {
						k++;
					} else
						k = 0;
				
					break;
				case 2:
					if (Double.compare(this.insertFromUnassigned(mandatory, true), 0) <= 0) {
						k++;
					} else
						k = 0;
				
					break;
				case 3:
					if (Double.compare(this.insertFromUnassigned(mandatory2, true), 0) <= 0) {
						k++;
					} else
						k = 0;
				
					break;
				case 4:
					if (Double.compare(this.insertFromUnassigned(optional, true), 0) <= 0) {
						k++;
					} else
						k = 0;
				
					break;
				case 5:
					if (Double.compare(twoOpt(true), 0) <= 0) {
						k++;
					} else
						k = 0;
					
					break;
					
				case 6:
					if (Double.compare(crossExchange(true), 0) <= 0) {
						k++;
					} else
						k = 0;
					
					break;
//					// commented out on ieee conference
//				case 6:
//					if (Double.compare(removeOptional(optional, true), 0) <= 0) {
//						k++;
//					} else
//						k = 0;
//					
//					break;
//					
				default:
					//this.optimizeRouteInPeriod(currentPeriodIndex);
					//System.out.println("before save::" +nw.getCommodityCount() + "---" + taskCount(mandatory, mandatory2, optional));
					if (saveBetterSolution(mandatory, mandatory2, optional)) {
						shakeI = 0;
						shakeBonus = 0;
					} else {
						shakeBonus++;
						tabuLength++;
						shakeI++;
						if (shakeI > 6)
							shakeI = 0;
					}
					//System.out.println("after save::" +nw.getCommodityCount() + "---" + taskCount(mandatory, mandatory2, optional));
					int shakeTime = 5 + shakeBonus > shakeBonusThreshold ? shakeBonus:0;
					if (shakeTime > convergenceThreshold) {
						shakeTime = 5;
						tabuLength = 5;
					}
					for (int j = 0; j < shakeTime; j++) {
						shake(mandatory, mandatory2, optional);
					}
					k = 0;
				}
			}
			
//			System.out.print("m: " + mandatory.size());
//			nw.returnPeriodTasks(mandatory);
//			System.out.print(" m2: " + mandatory2.size());
//			nw.returnPeriodTasks(mandatory2);
//			System.out.print(" op: " + optional.size());
//			nw.returnPeriodTasks(optional);
			nw.restoreSolution(bestSolution);
			this.optimizeAllRoutes();
			System.out.println(" OV: " + nw.getShiftObjectiveValues(i));
		}
		//TODO, restore each shift when it is completed!! not after all shifts are completed
		nw.restoreSolution(bestSolution);
		this.optimizeAllRoutes();
	}

	public int taskCount(Vector<Task> mandatory, 
			Vector<Task> mandatory2, Vector<Task> optional) {
		Vector<Task> n = new Vector<Task>();
		n.addAll(mandatory);
		n.addAll(mandatory2);
		n.addAll(optional);
		int count = 0;
		for (int i = 0; i < n.size(); i++) {
			if (n.get(i).gT == null) {
				count++;
			}
			else
			{
				count += 2;
			}
		}
		
		return count;
	}

	
	private boolean saveBetterSolution(Vector<Task> m1, 
			Vector<Task> m2, Vector<Task> o) {
		Vector<Task> otherTasks = new Vector<Task>();
		otherTasks.addAll(m1);
		otherTasks.addAll(m2);
		otherTasks.addAll(o);
		
		Solution currentSolution = new Solution(nw, otherTasks); 
		if (bestSolution == null) {
			bestSolution = currentSolution;
			System.out.print("|");
		} else {
			if (currentSolution.betterThan(bestSolution)) {
				System.out.print("|");
				bestSolution = currentSolution;
			} else {
				System.out.print(".");
				return false;
			}
		}
		
		return true;
	}
	
	/* Swapping neighbourhood.
	 * returns whether the solution is improved or not.
	 * the value is the deadheading time reduced.
	 * 
	 * loops through all TAs in routes, try swap with/insert into
	 * other routes.
	 */
	private double swap(boolean descend) {
		Vector<Route> routes = new Vector<Route>();
		int si = currentShiftIndex > 0 ? currentShiftIndex:0;
		int numberOfShifts = nw.getNumberOfShifts();
		for (; si < currentShiftIndex + 3 && si < numberOfShifts; si++) {
			routes.addAll(nw.routesOfShifts.get(si));
		}
		
		int numberOfRoutes = routes.size();
		Vector<Route> r1 = new Vector<Route>(),
				r2 = new Vector<Route>();
		Vector<Integer> index1 = new Vector<Integer>(),
				index2 = new Vector<Integer>();
		double bestEdReduce = 0;
		
		for (int i = 0; i < numberOfRoutes; i++) {
			Route currentRoute = routes.elementAt(i);
			int route1Length = currentRoute.taskSet.size();
			currentRoute.check();
			double cr_ed = currentRoute.emptyDistance + currentRoute.softTimeWindowPenalty;
			
			for (int j = 0; j < route1Length; j++) {
				if (currentRoute.taskSet.get(j).finished)
					continue;
				
				if (isTabu(currentRoute.taskSet.elementAt(j)))
					continue;
				// swapping test start
				for (int k = i; k < numberOfRoutes; k++) {
					Route swapRoute = routes.elementAt(k);
					int route2Length = swapRoute.taskSet.size();
					swapRoute.check();
					double sr_ed = swapRoute.emptyDistance + swapRoute.softTimeWindowPenalty;

					int l;
					if (k == i)
						l = j+1;
					else
						l = 0;
					
					for (; l < route2Length; l++) {
						if (swapRoute.taskSet.get(l).finished || 
								isTabu(swapRoute.taskSet.elementAt(l)) ||
								currentRoute.taskSet.elementAt(j).cmdt.id.equals(swapRoute.taskSet.elementAt(l).cmdt.id))
							continue;
						
						if (swapTask(currentRoute, j, swapRoute, l)) {
							double edReduce = cr_ed - currentRoute.emptyDistance - currentRoute.softTimeWindowPenalty;
							if (i != k)
								edReduce += sr_ed - swapRoute.emptyDistance - swapRoute.softTimeWindowPenalty;

							if (descend) {
								if (Double.compare(edReduce, bestEdReduce) > 0) {
									bestEdReduce = edReduce;
									r1.add(0, currentRoute);
									r2.add(0, swapRoute);
									index1.add(0, j);
									index2.add(0, l);
								}
							} else {
								if (Double.compare(edReduce, 0) != 0) {
									r1.add(currentRoute);
									r2.add(swapRoute);
									index1.add(j);
									index2.add(l);
								}
							}
							//swap back
							swapTask(currentRoute, j, swapRoute, l);
						}
						
					}
				} // end of swapping test
				
				
			}
		} // end of finding all swaps.
		
		if (descend) {
			if (Double.compare(bestEdReduce, 0) > 0) {
				addToTabu(r1.get(0).taskSet.get(index1.get(0)));
				addToTabu(r2.get(0).taskSet.get(index2.get(0)));
				swapTask(r1.get(0), index1.get(0), r2.get(0), index2.get(0));
				
			}
			return bestEdReduce;
		} else {
			if (r1.size() == 0) return -1;
			Random rg = new Random();
			int n = rg.nextInt(r1.size());
			addToTabu(r1.get(n).taskSet.get(index1.get(n)));
			addToTabu(r2.get(n).taskSet.get(index2.get(n)));
			swapTask(r1.get(n), index1.get(n), r2.get(n), index2.get(n));
			return 1;
		}
		
	}

	@SuppressWarnings("unchecked")
	private double move(boolean descend_mode) {
		int r_size = currentShift.size();
		Vector<Route> routes = new Vector<Route>();
		int si = currentShiftIndex > 0 ? currentShiftIndex:0;
		int numberOfShifts = nw.getNumberOfShifts();
		for (; si < currentShiftIndex + 3 && si < numberOfShifts; si++) {
			routes.addAll(nw.routesOfShifts.get(si));
		}
		
		
		Vector<Route> route1 = new Vector<Route>(),
				route2 = new Vector<Route>();
		Vector<Integer> index1 = new Vector<Integer>(),
				index2 = new Vector<Integer>();
		double bestEdReduce = 0;
		for (int i = 0; i < r_size; i++) {
			Route currentRoute = routes.elementAt(i);
			int cr_size = currentRoute.taskSet.size();
			currentRoute.check();
			double cr_ed = currentRoute.emptyDistance + currentRoute.softTimeWindowPenalty;
			for (int j = 0; j < cr_size; j++) {
				if (currentRoute.taskSet.elementAt(j).finished ||
						isTabu(currentRoute.taskSet.elementAt(j)))
					continue;
				
				// Insertion test start
				for (int k = 0; k < r_size; k++) {
					Route insertRoute = routes.elementAt(k);
					Vector<Task> ir_bak = (Vector<Task>) insertRoute.taskSet.clone();
					int ir_size = insertRoute.taskSet.size();
					insertRoute.check();
					double ir_ed = insertRoute.emptyDistance + insertRoute.softTimeWindowPenalty;
					
					for (int l = 0; l <= ir_size; l++) {
						if (l != ir_size && insertRoute.taskSet.get(l).finished)
							continue;

						if (moveTask(currentRoute, j, insertRoute, l)) {
							double edReduce = cr_ed - currentRoute.emptyDistance - currentRoute.softTimeWindowPenalty;
							if (i != k)
								edReduce += ir_ed - insertRoute.emptyDistance - insertRoute.softTimeWindowPenalty;

							if (descend_mode) {
								if (Double.compare(edReduce, bestEdReduce) > 0) {
									bestEdReduce = edReduce;
									route1.add(0, currentRoute);
									route2.add(0, insertRoute);
									index1.add(0, j);
									index2.add(0, l);
								}
							} else {
								if (Double.compare(edReduce, 0) != 0) {
									route1.add(currentRoute);
									route2.add(insertRoute);
									index1.add(j);
									index2.add(l);
								}
							}
							
							//revert back
							if (insertRoute == currentRoute) {
								insertRoute.taskSet = (Vector<Task>) ir_bak.clone();
								//System.out.println("Successfully done single route ins");
							} else {
								moveTask(insertRoute, l, currentRoute, j);
							}
						}
					} // Slot finding loop
				} // Route finding loop
				
				
			}
		} // end of finding all inserts.
		
		if (descend_mode) {
			if (Double.compare(bestEdReduce, 0) > 0) {
				addToTabu(route1.get(0).taskSet.elementAt(index1.get(0)));
				moveTask(route1.get(0), index1.get(0), route2.get(0), index2.get(0));
			}
			return bestEdReduce;
			
		} else {
			if (route1.size() == 0) return -1;
			Random rg = new Random();
			int n = rg.nextInt(route1.size());
			//System.out.println("Shaking: " + n + " of " + route2.size());
			
			addToTabu(route1.get(n).taskSet.get(index1.get(n)));
			moveTask(route1.get(n), index1.get(n), route2.get(n), index2.get(n));
			return 1;
		}
		
	}
	
	/* It inserts unassigned ta from a ta vector.
	 * Return:
	 * EmptyDistanceRate reduced
	 * or 0 if no improvement;
	 */
	private double insertFromUnassigned(Vector<Task> unassigned, boolean descend) {
		int ta_size = unassigned.size();
		double rsize = currentShift.size();
		
		Vector<Route> r = new Vector<Route>();
		Vector<Integer> slot = new Vector<Integer>();
		Vector<Task> ta = new Vector<Task>();
		
		double BestEmptyRateDecrease = 0;
		for (int i = 0; i < rsize; i++) {
			Route croute = currentShift.get(i);
			int cr_size = croute.taskSet.size();
			croute.check();
			double emptyRateO = 
					(croute.emptyDistance + croute.softTimeWindowPenalty) / croute.loadedDistance;
			for (int j = 0; j <= cr_size; j++) { // slot size == cr_size.
				if (j != cr_size && croute.taskSet.get(j).finished)
					continue;
				
				for (int k = 0; k < ta_size; k++) {
					//System.out.printf("%d %d %d\n", i, j, k);
					Task t = unassigned.get(k);
					if (isTabu(t))
						continue;
					
					croute.insert(k, j, unassigned);
					if (croute.check() == 1) {
						if (descend) {
							double emptyRateDecrease =
									emptyRateO - 
									(croute.emptyDistance + croute.softTimeWindowPenalty) / croute.loadedDistance;
							if (Double.compare(emptyRateDecrease, BestEmptyRateDecrease) > 0) {
								BestEmptyRateDecrease = emptyRateDecrease;
								r.add(0, croute);
								slot.add(0, j);
								ta.add(0, t);
							}
							
						} else {
							r.add(croute);
							slot.add(j);
							ta.add(t);
						}
					}
					croute.remove(j, unassigned);
				}
			}
		}
		
		if (descend) {
			if (Double.compare(BestEmptyRateDecrease, 0) > 0) {
				addToTabu(ta.get(0));
				r.get(0).insert(unassigned.indexOf(ta.get(0)), slot.get(0), unassigned);
			}
			
			return BestEmptyRateDecrease;
			
		} else {
			if (r.size() == 0) return -1;
			Random rg = new Random();
			int n = rg.nextInt(r.size());
			//System.out.println("Shaking: " + n + " of " + route2.size());
			
			addToTabu(ta.get(n));
			r.get(n).insert(unassigned.indexOf(ta.get(n)), slot.get(n), unassigned);
			return 1;
		}
		//System.out.println("Returned: " + BestEmptyRateDecrease);
		
	}
	
	private double removeOptional(Vector<Task> optional, boolean descend) {
		double rsize = currentShift.size();
		Vector<Route> route = new Vector<Route>();
		Vector<Integer> index = new Vector<Integer>();
		double bestEmptyReduce = 0;
		for (int i = 0; i < rsize; i++) {
			Route croute = currentShift.get(i);
			croute.check();
			int cr_size = croute.taskSet.size();
			double emptyRateO = (croute.emptyDistance + croute.softTimeWindowPenalty) / croute.loadedDistance;
			for (int j = 0; j < cr_size; j++) {
				if (croute.taskSet.get(j).finished)
					continue;
				
				Task t = croute.taskSet.get(j);
				// To prevent removing mandatory task
				//if (t.cmdt.latestPeriod == this.currentPeriodIndex || isTabu(t))
				// To prevent removing mandatory tasks from next 2 periods and this period
				if (t.cmdt.latestPeriod <= this.currentShiftIndex + 1 || isTabu(t))
					continue;
				
				croute.taskSet.remove(j);
				//Don't remove the last node in the route
				if (croute.check() == 1 && croute.taskSet.size() > 0) {
					if (descend) {
						double emptyRateReduce = 
								emptyRateO - (croute.emptyDistance + croute.softTimeWindowPenalty) / croute.loadedDistance;
						
						if (Double.compare(emptyRateReduce, bestEmptyReduce) > 0) {
							bestEmptyReduce = emptyRateReduce;
							route.add(0, croute);
							index.add(0, j);
						}
						
					} else {
						route.add(croute);
						index.add(j);
					}
				}
				croute.taskSet.add(j, t);
			}
		}
		
		if (descend) {
			if (bestEmptyReduce != 0) {
				//System.out.println("emptyReduce:" + bestEmptyReduce);
				addToTabu(route.get(0).taskSet.get(index.get(0)));
				route.get(0).remove(index.get(0), optional);
			}
			return bestEmptyReduce;
			
		} else {
			if (route.size() == 0) return -1;
			Random rg = new Random();
			int n = rg.nextInt(route.size());
			//System.out.println("Shaking: " + n + " of " + route2.size());
			
			addToTabu(route.get(n).taskSet.get(index.get(n)));
			route.get(n).remove(index.get(n), optional);
			return 1;
		}
		
	}
	
	//TODO: isFinished and Tabu not implemented
	private double twoOpt(boolean descend) {
		Vector<Route> routes = new Vector<Route>();
		routes.addAll(currentShift);
		if (this.currentShiftIndex > 0)
			routes.addAll(nw.routesOfShifts.get(currentShiftIndex - 1));
		
		int numberOfShifts = nw.getNumberOfShifts();
		if (currentShiftIndex < numberOfShifts - 1)
			routes.addAll(nw.routesOfShifts.get(currentShiftIndex + 1));
		
		// ------- best results here
		double bestEdReduce = 0;
		Vector<Route> r1v = new Vector<Route>(),
				r2v = new Vector<Route>();
		Vector<Integer> slot1 = new Vector<Integer>(),
				slot2 = new Vector<Integer>();
		
		// ------- loop
		int numberOfRoutes = routes.size();
		for (int i = 0; i < numberOfRoutes; i++) {
			Route r1 = routes.get(i);
			int taskSetSize1 = r1.taskSet.size();
			if (taskSetSize1 < 4) continue;
			r1.check();
			double ed1 = r1.emptyDistance + r1.softTimeWindowPenalty;
			
			for (int j = i + 1; j < numberOfRoutes; j++) {
				Route r2 = routes.get(j);
				int taskSetSize2 = r2.taskSet.size();
				if (taskSetSize2 < 4) continue;
				r2.check();
				double ed2 = r2.emptyDistance + r2.softTimeWindowPenalty;
				// Loop through slots: (k = 0) => slot after 0th ta. 
				for (int k = 2; k < taskSetSize1 - 2; k++) {
					if (r1.taskSet.get(k).finished || isTabu(r1.taskSet.get(k)))
						continue;
					for (int l = 2; l < taskSetSize2 - 2; l++) {
						if (r2.taskSet.get(l).finished || isTabu(r2.taskSet.get(l)))
							continue;
						if (twoOpt(r1, k, r2, l, false)) {
							double edReduce = ed1 + ed2 - 
									(r1.emptyDistance + r1.softTimeWindowPenalty 
											+ r2.emptyDistance + r2.softTimeWindowPenalty);
							if (descend) {
								if (Double.compare(edReduce, bestEdReduce) > 0) {
									r1v.add(0, r1);
									r2v.add(0, r2);
									slot1.add(0, k);
									slot2.add(0, l);
									bestEdReduce = edReduce;
								}
								
							} else {
								if (Double.compare(edReduce, 0) != 0) {
									r1v.add(r1);
									r2v.add(r2);
									slot1.add(new Integer(k));
									slot2.add(new Integer(l));
								}
							}
						}
					} // slot 2
				} // slot1
			}// r2
		}// r1

		
		if (descend) {
			if (Double.compare(bestEdReduce, 0) > 0) {
				twoOpt(r1v.get(0), slot1.get(0), r2v.get(0), slot2.get(0), true);
			}
			return bestEdReduce;
		} else {
			if (r1v.size() == 0) return 0;
			Random rg = new Random();
			int n = rg.nextInt(r1v.size());
			twoOpt(r1v.get(n), slot1.get(n), r2v.get(n), slot2.get(n), true);
			return 1;
		}
			
		
	}
	
	public double crossExchange(boolean descend)
	{
		Vector<Route> routes = new Vector<Route>();
		routes.addAll(currentShift);
		if (this.currentShiftIndex > 0)
			routes.addAll(nw.routesOfShifts.get(currentShiftIndex - 1));
		
		int numberOfShifts = nw.getNumberOfShifts();
		if (currentShiftIndex < numberOfShifts - 1)
			routes.addAll(nw.routesOfShifts.get(currentShiftIndex + 1));
		
		// ------- best results here
		double bestEdReduce = 0;
		Vector<Route> r1v = new Vector<Route>(),
				r2v = new Vector<Route>();
		Vector<Integer> from1v = new Vector<Integer>(), length1v = new Vector<Integer>(),
				from2v = new Vector<Integer>(), length2v = new Vector<Integer>();
		
		// ------- loop
		int routesSize = routes.size();
		for (int i = 0; i < routesSize; i++) {
			Route r1 = routes.get(i);
			int r1length = r1.taskSet.size();
			if (r1length < 3) continue;
			r1.check();
			double ed1 = r1.emptyDistance + r1.softTimeWindowPenalty;
			
			for (int j = i + 1; j < routesSize; j++) {
				Route r2 = routes.get(j);
				int r2length = r2.taskSet.size();
				if (r2length < 3) continue;
				r2.check();
				double ed2 = r2.emptyDistance + r2.softTimeWindowPenalty;
				
				for (int from1 = 0; from1 + 2 <= r1length; from1++) {
					
					for (int length1 = 2; length1 < 4 && from1 + length1 <= r1length; length1++) {
						for (int from2 = 0; from2 + 2 <= r2length; from2++) {
							for(int length2 = 2; length2 < 4 && from2 + length2 <= r2length; length2++) {
								if (cross(r1, from1, length1, r2, from2, length2, false)) {
									double edReduce = ed1 + ed2 - 
											(r1.emptyDistance + r1.softTimeWindowPenalty 
													+ r2.emptyDistance + r2.softTimeWindowPenalty);
									if (descend) {
										if (Double.compare(edReduce, bestEdReduce) > 0) {
											r1v.add(0, r1);
											r2v.add(0, r2);
											from1v.add(0, from1);
											from2v.add(0, from2);
											length1v.add(0, length1);
											length2v.add(0, length2);
											bestEdReduce = edReduce;
										}
										
									} else {
										if (Double.compare(edReduce, 0) != 0) {
											r1v.add(r1);
											r2v.add(r2);
											from1v.add(from1);
											from2v.add(from2);
											length1v.add(length1);
											length2v.add(length2);
										}
									}
								}
							} //length2
						} // from2
					} // length1
				}//from1
				
			}
		}
		
		if (descend) {
			if (Double.compare(bestEdReduce, 0) > 0) {
				// cross that
				cross(r1v.get(0), from1v.get(0), length1v.get(0), r2v.get(0), from2v.get(0), length2v.get(0), true);
			}
			return bestEdReduce;
			
		} else {
			if (r1v.size() == 0) return 0;
			Random rg = new Random();
			int n = rg.nextInt(r1v.size());
			cross(r1v.get(n), from1v.get(n), length1v.get(n), r2v.get(n), from2v.get(n), length2v.get(n), true);
			return 1;
		}
			
		
	}
	
	
	/*
	 * Randomly optimize a segment of a route in each route
	 */
	private void randomSegmentOpt(int segmentLen) {
		int numberOfRoutes = currentShift.size();
		for (int i = 0; i < numberOfRoutes; i++) {
			Route r = currentShift.get(i);
			int rsize = r.taskSet.size();
			int start = 0, end = rsize - 1;
			if (rsize > segmentLen) { 
				Random rg = new Random();
				start = rg.nextInt(rsize - segmentLen + 1);
				end = start + segmentLen - 1;
			}
			
			//System.out.println("start: " + start + " end: " + end);
			optimizePartialRoute(r, start, end);
		}
	}
	
	private boolean shake
	(Vector<Task> mandatory, Vector<Task> mandatory2, Vector<Task> optional) {
		switch(shakeI) {
		case 0:
			if (insertFromUnassigned(mandatory, true) > 0) return true;
			break;
		case 1:
			if (insertFromUnassigned(mandatory2, true) > 0) return true;
			break;
		case 2:
			if (insertFromUnassigned(optional, true) > 0) return true;
			break;
		case 3:
			//System.out.println("shake using insertion.");
			if (move(false) > 0) return true;
			break;
		case 4:
			//System.out.println("shake using swap.");
			if (swap(false) > 0) return true;
			break;
		case 5:
			if (removeOptional(optional, true) > 0) return true;
			break;
		case 6:
			if (Double.compare(crossExchange(false), 0) > 0) return true;
			break;

		default:
			System.out.println("Invalid shakeI");
			break;			
		}
		
		return false;
	}
}

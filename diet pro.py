import tkinter as tk
import json
class GymManagementSystem:
def __init__(self, root):
self.root = root
self.root.title("Gym Management System")
self.main_frame = tk.Frame(root)
self.main_frame.pack(padx=20, pady=20)
self.member_id_label = tk.Label(self.main_frame, text="Member ID:")
self.member_id_label.grid(row=0, column=0)
self.member_id_entry = tk.Entry(self.main_frame)
self.member_id_entry.grid(row=0, column=1)
self.member_name_label = tk.Label(self.main_frame, text="Member Name:")
self.member_name_label.grid(row=1, column=0)
self.member_name_entry = tk.Entry(self.main_frame)
self.member_name_entry.grid(row=1, column=1)
self.member_age_label = tk.Label(self.main_frame, text="Member Age:")
self.member_age_label.grid(row=2, column=0)
self.member_age_entry = tk.Entry(self.main_frame)
self.member_age_entry.grid(row=2, column=1)
self.membership_fee_label = tk.Label(self.main_frame, text="Membership Fee:")
self.membership_fee_label.grid(row=3, column=0)
self.membership_fee_entry = tk.Entry(self.main_frame)
self.membership_fee_entry.grid(row=3, column=1)
self.add_member_button = tk.Button(self.main_frame, text="Add Member",
command=self.add_member)
self.add_member_button.grid(row=4, column=0)
self.show_members_button = tk.Button(self.main_frame, text="Show Members",
command=self.show_members)
self.show_members_button.grid(row=4, column=1)
self.delete_member_button = tk.Button(self.main_frame, text="Delete Member",
command=self.delete_member)
self.delete_member_button.grid(row=5, column=0)
self.diet_plan_button = tk.Button(self.main_frame, text="Diet Plan",
command=self.open_diet_plan_window)
self.diet_plan_button.grid(row=5, column=1)
self.status_label = tk.Label(self.main_frame, text="")
self.status_label.grid(row=6, column=0, columnspan=2)
self.member_list_text = tk.Text(self.main_frame, height=10, width=40)
self.member_list_text.grid(row=7, column=0, columnspan=2)
def save_data_to_json(self):
with open("gymdata.json", "w") as json_file:
json.dump(self.members_data, json_file)
def add_member(self):
member_id = self.member_id_entry.get()
member_name = self.member_name_entry.get()
member_age = self.member_age_entry.get()
membership_fee = self.membership_fee_entry.get()
if not all([member_id, member_name, member_age, membership_fee]):
self.status_label.config(text="Please fill in all fields")
return
member = {
"id": member_id,
"name": member_name,
"age": member_age,
"membership_fee": membership_fee
}
self.members_data.append(member)
self.save_data_to_json()
self.status_label.config(text="Member added successfully")
self.member_id_entry.delete(0, tk.END)
self.member_name_entry.delete(0, tk.END)
self.member_age_entry.delete(0, tk.END)
self.membership_fee_entry.delete(0, tk.END)
def show_members(self):
self.member_list_text.delete(1.0, tk.END)
for member in self.members_data:
self.member_list_text.insert(tk.END, f"ID: {member['id']}, Name: {member['name']},
Age: {member['age']}, Fee: ${member['membership_fee']} per month\n")
def open_diet_plan_window(self):
diet_plan_window = tk.Toplevel(self.root)
app2 = DietaryPlanSuggestion(diet_plan_window)
def delete_member(self):
member_id = self.member_id_entry.get()
if not member_id:
self.status_label.config(text="Please enter a Member ID to delete.")
return
deleted = False
for member in self.members_data:
if member['id'] == member_id:
self.members_data.remove(member)
self.save_data_to_json()
self.status_label.config(text=f"Member with ID {member_id} deleted
successfully.")
deleted = True
break
if not deleted:
self.status_label.config(text=f"Member with ID {member_id} not found.")
class DietaryPlanSuggestion:
def __init__(self, root):
self.root = root
self.root.title("Dietary Plan Suggestion")
self.main_frame = tk.Frame(root)
self.main_frame.pack(padx=20, pady=20)
self.age_label = tk.Label(self.main_frame, text="Enter your age:")
self.age_label.grid(row=0, column=0)
self.age_entry = tk.Entry(self.main_frame)
self.age_entry.grid(row=0, column=1)
self.goal_label = tk.Label(self.main_frame, text="Select your goal:")
self.goal_label.grid(row=1, column=0)
self.goal_var = tk.StringVar()
self.goal_var.set("weight_loss")
self.goal_menu = tk.OptionMenu(self.main_frame, self.goal_var, "weight_loss",
"muscle_gain")
self.goal_menu.grid(row=1, column=1)
self.generate_button = tk.Button(self.main_frame, text="Generate Plan",
command=self.generate_dietary_plan)
self.generate_button.grid(row=2, column=0, columnspan=2)
self.result_text = tk.Text(self.main_frame, height=10, width=40)
self.result_text.grid(row=3, column=0, columnspan=2)
self.dietary_plans = {
"weight_loss": {
"1": {
"breakfast": "Greek yogurt with mixed berries and honey",
"lunch": "Grilled chicken breast with quinoa and roasted vegetables",
"dinner": "Baked salmon with asparagus and brown rice",
"snacks": "Vegetable sticks with hummus"
},
"2": {
"breakfast": "Avocado toast with poached eggs",
"lunch": "Mixed greens salad with grilled shrimp and vinaigrette",
"dinner": "Quinoa-stuffed bell peppers with lean ground turkey",
"snacks": "Handful of almonds and an apple"
},
"3": {
"breakfast": "Chia seed pudding with fresh fruits",
"lunch": "Grilled fish with quinoa and steamed vegetables",
"dinner": "Baked chicken with wild rice and mixed greens",
"snacks": "Yogurt with nuts and berries"
},
"4": {
"breakfast": "Oatmeal with sliced banana and nuts",
"lunch": "Grilled chicken breast with steamed vegetables",
"dinner": "Baked fish with quinoa and green salad",
"snacks": "Greek yogurt with honey and walnuts"
},
"0": {
"breakfast": "Whole grain cereal with low-fat milk and a banana",
"lunch": "Grilled chicken breast with mixed greens and vinaigrette",
"dinner": "Baked fish with brown rice and steamed vegetables",
"snacks": "Carrot sticks with hummus"
}
},
"muscle_gain": {
"1": {
"breakfast": "Protein smoothie with bananas, protein powder, and almond milk",
"lunch": "Grilled steak with sweet potato and green beans",
"dinner": "Oven-baked chicken thighs with brown rice and broccoli",
"snacks": "Cottage cheese with pineapple chunks"
},
"2": {
"breakfast": "Whole grain pancakes with berries and maple syrup",
"lunch": "Tuna salad with mixed greens and whole wheat crackers",
"dinner": "Grilled chicken breast with whole grain pasta and marinara sauce",
"snacks": "Protein bar and a banana"
},
"3": {
"breakfast": "Egg white omelette with spinach and whole grain toast",
"lunch": "Salmon salad with mixed greens and balsamic vinaigrette",
"dinner": "Lean beef stir-fry with brown rice and vegetables",
"snacks": "Protein smoothie with almond milk and banana"
},
"4": {
"breakfast": "Cottage cheese with pineapple and whole grain toast",
"lunch": "Grilled salmon with sweet potato and green beans",
"dinner": "Lean turkey meatballs with whole wheat pasta and marinara sauce",
"snacks": "Protein shake with low-fat milk"
},
"0": {
"breakfast": "Whole grain pancakes with Greek yogurt and mixed berries",
"lunch": "Grilled chicken breast with quinoa and steamed vegetables",
"dinner": "Baked salmon with sweet potato and asparagus",
"snacks": "Protein-rich granola bar and a glass of milk"
}
}
}
def generate_dietary_plan(self):
age_text = self.age_entry.get()
goal = self.goal_var.get()
if not age_text:
self.result_text.delete(1.0, tk.END)
self.result_text.insert(tk.END, "Please enter a valid age.")
return
age = int(age_text)
dietary_plan = self.get_dietary_plan(age, goal)
if dietary_plan:
self.result_text.delete(1.0, tk.END)
self.result_text.insert(tk.END, dietary_plan)
else:
self.result_text.delete(1.0, tk.END)
self.result_text.insert(tk.END, "No dietary plan available for this age and goal.")
def get_dietary_plan(self, age, goal):
age_group = None
if age >= 18 and age <= 30:
age_group = "1"
elif age >= 31 and age <= 45:
age_group = "2"
elif age >= 46 and age <= 60:
age_group = "3"
elif age > 60:
age_group = "4"
else:
age_group = "0"
dietary_plan = self.dietary_plans.get(goal, {}).get(age_group, None)
return dietary_plan
if __name__ == "__main__":
root = tk.Tk()
app = GymManagementSystem(root)
app.members_data = [] # Initialize members data
root.mainloop()
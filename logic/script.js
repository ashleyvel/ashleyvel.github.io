function calculateBudget() {
    const income = parseFloat(document.getElementById("income").value);

    if (isNaN(income) || income <= 0) {
        alert("Please enter a valid income amount.");
        return;
    }

    const needs = (income * 0.50).toFixed(2);
    const wants = (income * 0.30).toFixed(2);
    const savings = (income * 0.20).toFixed(2);

    document.getElementById("needs").textContent = needs;
    document.getElementById("wants").textContent = wants;
    document.getElementById("savings").textContent = savings;
}

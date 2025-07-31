import React, { useState } from "react";
import axios from "axios";

export default function App() {
  const [form, setForm] = useState({
    initialDate: "",
    finalDate: "",
    firstPaymentDate: "",
    totalAmount: "",
    interestRate: "",
  });

  const [installments, setInstallments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const simulate = async () => {
    setLoading(true);
    setError(null);
    try {
      // Converter taxa de juros para decimal
      const interestDecimal = parseFloat(form.interestRate) / 100;
      
      // Converter valor do empréstimo para número inteiro
      const totalAmountValue = parseInt(form.totalAmount, 10) || 0;

      const payload = {
        initialDate: form.initialDate,
        finalDate: form.finalDate,
        firstPaymentDate: form.firstPaymentDate,
        totalAmount: totalAmountValue,
        interestRate: interestDecimal,
      };

      const response = await axios.post(
        "http://localhost:8080/api/v1/loan",
        payload
      );

      setInstallments(response.data.installments || []);
    } catch (error) {
      console.error("Erro ao simular empréstimo:", error);
      setError("Erro ao simular empréstimo. Verifique os dados e tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  // Função para formatar valores monetários
  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(value);
  };

  // Função para formatar datas (yyyy-MM-dd -> dd/MM/yyyy)
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const [year, month, day] = dateString.split('-');
    return `${day}/${month}/${year}`;
  };

  const formFields = [
    { label: "Data Inicial", name: "initialDate", type: "date" },
    { label: "Data Final", name: "finalDate", type: "date" },
    { label: "Primeiro Pagamento", name: "firstPaymentDate", type: "date" },
    { 
      label: "Valor do Empréstimo (R$)", 
      name: "totalAmount", 
      type: "number",
      min: "0",
      step: "1"
    },
    {
      label: "Taxa de Juros (%)",
      name: "interestRate",
      type: "number",
      step: "0.01",
    },
  ];

  return (
    <div className="max-w-7xl mx-auto px-4 py-10">
      <h1 className="text-3xl font-bold mb-6 text-center">
        Simulador de Empréstimos
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        {formFields.map(({ label, name, type, min, step }) => (
          <div key={name} className="flex flex-col">
            <label className="font-medium">{label}</label>
            <input
              name={name}
              type={type}
              min={min}
              step={step}
              value={form[name]}
              onChange={handleChange}
              required
              className="mt-2 p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent shadow-sm"
            />
          </div>
        ))}
      </div>

      <div className="text-center mb-6">
        <button
          onClick={simulate}
          disabled={loading}
          className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition min-w-[200px]"
        >
          {loading ? (
            <span className="flex items-center justify-center">
              <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Calculando...
            </span>
          ) : "Calcular"}
        </button>
      </div>

      {error && <p className="mt-4 text-red-600 text-center">{error}</p>}

      {installments.length > 0 && (
        <div className="mt-8 overflow-auto shadow-lg rounded-lg">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-100">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Data Competência</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Valor de empréstimo</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Saldo Devedor</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Consolidada</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Total</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Amortização</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Saldo</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Provisão</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Acumulado</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Pago</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {installments.map((item, index) => (
                <tr 
                  key={index} 
                  className={item.installmentNumber ? "bg-blue-50 font-medium" : "hover:bg-gray-50"}
                >
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700">
                    {formatDate(item.competenceDate)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.loanAmount)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.outstandingBalance)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-center">
                    {item.installmentNumber}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.total)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.amortization)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.remainingBalance)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.provision)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.accumulated)}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-sm text-gray-700 text-right">
                    {formatCurrency(item.paidInterest)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}